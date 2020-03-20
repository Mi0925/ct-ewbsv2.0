package cn.comtom.reso.main.file.controller;


import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.domain.reso.file.request.FileUpdateRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.file.entity.dbo.TFile;
import cn.comtom.reso.main.file.service.ITFileService;
import cn.comtom.tools.enums.StateDictEnum;
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
@RequestMapping("/res/file")
@Api(tags = "文件管理")
@Slf4j
public class TFileController extends BaseController {

    @Autowired
    private ITFileService itFileService;


    @PostMapping("/save")
    @ApiOperation(value = "保存文件信息", notes = "保存文件信息")
    public ApiEntityResponse<FileInfo> save(@RequestBody @Valid FileAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("enter FileInfo save . FileAddRequest=[{}]",request);
        }
        TFile file = new TFile();
        BeanUtils.copyProperties(request,file);
        file.setId(UUIDGenerator.getUUID());
        file.setCreateDate(new Date());
        if(StringUtils.isBlank(file.getAuditState())){
            file.setAuditState(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        }
        itFileService.save(file);
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(file,fileInfo);
        return ApiEntityResponse.ok(fileInfo);

    }


    @GetMapping("/getByMd5")
    @ApiOperation(value = "根据MD5查询文件详情", notes = "根据MD5查询文件详情")
    public ApiEntityResponse<FileInfo> getByMd5(@RequestParam(name = "md5Code") String md5Code) {
        FileInfo fileInfo = itFileService.getByMd5(md5Code);
        if(fileInfo == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.FILE_NOT_EXSITS);
        }
        return ApiEntityResponse.ok(fileInfo);
    }

    @GetMapping("/{fileId}")
    @ApiOperation(value = "根据FileId查询文件详情", notes = "根据FileId查询文件详情")
    public ApiEntityResponse<FileInfo> getByFileId(@PathVariable(name = "fileId") String fileId) {
        TFile file = itFileService.selectById(fileId);
        if(file == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.FILE_NOT_EXSITS);
        }
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(file,fileInfo);
        return ApiEntityResponse.ok(fileInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询文件列表", notes = "分页查询文件列表")
    public ApiPageResponse<FileInfo> getByPage(@ModelAttribute FilePageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("get File info by page request=[{}]",request);
        }
        startPage(request);
        List<FileInfo> fileInfoList = itFileService.getList(request);
        return ApiPageResponse.ok(fileInfoList,page);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新文件信息", notes = "更新文件信息")
    public ApiResponse update(@RequestBody @Valid FileUpdateRequest request ,BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("update File info  request=[{}]",request);
        }
        TFile file = new TFile();
        BeanUtils.copyProperties(request,file);
        itFileService.update(file);
        return ApiResponse.ok();
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新文件信息", notes = "批量更新文件信息")
    public ApiEntityResponse<Integer> updateBatch(@RequestBody @Valid List<FileUpdateRequest> requests, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update FileInfo batch . requests=[{}]",requests);
        }
        Long count = Optional.ofNullable(requests).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> {
                    TFile file = new TFile();
                    BeanUtils.copyProperties(request,file);
                    try {
                        itFileService.update(file);
                        return true;
                    }catch (Exception e ){
                        log.error("update fileInfo fileId=[{}] error：",file.getId(), e);
                        return false;
                    }
                }).count();
        return ApiEntityResponse.ok(count.intValue());

    }

    @DeleteMapping("/delete/{fileId}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    public ApiResponse delete(@PathVariable(name = "fileId") String fileId) {
        if(log.isDebugEnabled()){
            log.debug("delete File info by fileId fileId=[{}]",fileId);
        }
        if(StringUtils.isBlank(fileId)){
            return  ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        itFileService.deleteById(fileId);
        return ApiResponse.ok();
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除文件", notes = "批量删除文件")
    public ApiEntityResponse<Integer> deleteBatch(@RequestBody List<String> fileIds) {
        if(log.isDebugEnabled()){
            log.debug("delete File info batch by fileIds fileIds=[{}]",fileIds);
        }
        if(fileIds== null || fileIds.isEmpty()){
            return  ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        return ApiEntityResponse.ok(itFileService.deleteByIds(fileIds));
    }

}
