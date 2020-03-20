package cn.comtom.reso.main.file.controller;


import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryAddRequest;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.domain.reso.file.request.FileLibraryUpdateRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.file.entity.dbo.FileLibrary;
import cn.comtom.reso.main.file.service.IFileLibraryService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/res/fileLibrary")
@Api(tags = "文件夹管理")
@Slf4j
public class FileLibraryController extends BaseController {

    @Autowired
    private IFileLibraryService fileLibraryService;

    @PostMapping("/save")
    @ApiOperation(value = "保存文件夹信息", notes = "保存文件夹信息")
    public ApiEntityResponse<FileLibraryInfo> save(@RequestBody @Valid FileLibraryAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~]enter FileLibraryInfo save . FileLibraryAddRequest=[{}]",request);
        }
        FileLibrary fileLibrary = new FileLibrary();
        BeanUtils.copyProperties(request,fileLibrary);
        fileLibrary.setLibId(UUIDGenerator.getUUID());
        fileLibrary.setCreateDate(new Date());
        fileLibraryService.save(fileLibrary);
        FileLibraryInfo fileLibraryInfo = new FileLibraryInfo();
        BeanUtils.copyProperties(fileLibrary,fileLibraryInfo);
        return ApiEntityResponse.ok(fileLibraryInfo);

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询文件夹列表", notes = "分页查询文件夹列表")
    public ApiPageResponse<FileLibraryInfo> page(@ModelAttribute FileLibraryPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~]query FileLibraryInfo by page . request=[{}]",request);
        }
        startPage(request);
        List<FileLibraryInfo> fileLibraryInfoList = fileLibraryService.getList(request);
        return ApiPageResponse.ok(fileLibraryInfoList,page);

    }

    @GetMapping("/{libId}")
    @ApiOperation(value = "查询文件夹详情", notes = "查询文件夹详情")
    public ApiEntityResponse<FileLibraryInfo> getById(@PathVariable(name = "libId") String libId) {
        if(log.isDebugEnabled()){
            log.debug("[!~]get  FileLibraryInfo by Id . libId=[{}]",libId);
        }
        FileLibrary fileLibrary = fileLibraryService.selectById(libId);
        if(fileLibrary == null){
           return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        FileLibraryInfo fileLibraryInfo = new FileLibraryInfo();
        BeanUtils.copyProperties(fileLibrary,fileLibraryInfo);
        return ApiEntityResponse.ok(fileLibraryInfo);

    }

    @PutMapping("/update")
    @ApiOperation(value = "更新文件夹信息", notes = "更新文件夹信息")
    public ApiResponse update(@RequestBody @Valid FileLibraryUpdateRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update FileLibraryInfo  . request=[{}]",request);
        }
        FileLibrary fileLibrary = new FileLibrary();
        BeanUtils.copyProperties(request,fileLibrary);
        fileLibraryService.update(fileLibrary);
        return ApiResponse.ok();

    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新文件夹信息", notes = "批量更新文件夹信息")
    public ApiEntityResponse<Long> updateBatch(@RequestBody @Valid List<FileLibraryUpdateRequest> requests, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update FileLibraryInfo batch . requests=[{}]",requests);
        }
        Long count = Optional.ofNullable(requests).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> {
                    FileLibrary fileLibrary = new FileLibrary();
                    BeanUtils.copyProperties(request,fileLibrary);
                    try {
                        fileLibraryService.update(fileLibrary);
                        return true;
                    }catch (Exception e ){
                        log.error("update fileLibrary libId=[{}] error：",fileLibrary.getLibId(), e);
                        return false;
                    }
                }).count();
        return ApiEntityResponse.ok(count);

    }

    @DeleteMapping("/delete/{libId}")
    @ApiOperation(value = "删除文件夹信息", notes = "删除文件夹信息")
    public ApiResponse delete(@PathVariable(name = "libId") String libId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete FileLibraryInfo by libId . libId=[{}]",libId);
        }
        if(StringUtils.isBlank(libId)){
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        fileLibraryService.deleteById(libId);
        return ApiResponse.ok();

    }

    @DeleteMapping("/deleteThisAndSubAll/{libId}")
    @ApiOperation(value = "删除文件夹及所有子文件（夹）", notes = "删除文件夹及所有子文件（夹）")
    public ApiEntityResponse<Integer> deleteThisAndSubAll(@PathVariable(name = "libId") String libId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete FileLibraryInfo by libId . libId=[{}]",libId);
        }
        if(StringUtils.isBlank(libId)){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Integer count = fileLibraryService.deleteThisAndSubAll(libId);
        return ApiEntityResponse.ok(count);

    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除文件夹信息", notes = "批量删除文件夹信息")
    public ApiEntityResponse<Integer> deleteBatch(@RequestBody List<String> libIds) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete FileLibraryInfo batch by libIds . libIds=[{}]",libIds);
        }
        if(libIds ==null || libIds.isEmpty()){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        return ApiEntityResponse.ok(fileLibraryService.deleteByIds(libIds));

    }




}
