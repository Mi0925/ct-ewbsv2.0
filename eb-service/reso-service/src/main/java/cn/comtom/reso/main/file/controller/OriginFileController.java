package cn.comtom.reso.main.file.controller;


import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFilePageRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.file.entity.dbo.OriginFile;
import cn.comtom.reso.main.file.service.IOriginFileService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/res/originFile")
@Api(tags = "接入文件管理")
@Slf4j
public class OriginFileController extends BaseController {

    @Autowired
    private IOriginFileService originFileService;

    @GetMapping("/{fileId}")
    @ApiOperation(value = "根据ID查询文件详情", notes = "根据ID查询文件详情")
    public ApiEntityResponse<OriginFileInfo> detail(@PathVariable(name = "fileId") String fileId) {
        OriginFile originFile = originFileService.selectById(fileId);
        if(originFile == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.FILE_NOT_EXSITS);
        }
        OriginFileInfo info = new OriginFileInfo();
        BeanUtils.copyProperties(originFile,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByMd5")
    @ApiOperation(value = "根据MD5查询文件详情", notes = "根据MD5查询文件详情")
    public ApiEntityResponse<OriginFileInfo> getByMd5(@RequestParam(name = "md5Code") String md5Code) {
        OriginFileInfo originFile = originFileService.getByMd5(md5Code);
        if(originFile == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.FILE_NOT_EXSITS);
        }
        return ApiEntityResponse.ok(originFile);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入文件信息", notes = "保存接入文件信息")
    public ApiEntityResponse<OriginFileInfo> save(@RequestBody @Valid OriginFileAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("enter OriginFileInfo save . OriginFileAddRequest=[{}]",request);
        }
        OriginFileInfo info = originFileService.save(request);
        if(info == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.FILE_SAVE_FAILURE);
        }
        return ApiEntityResponse.ok(info);

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询接入文件列表", notes = "分页查询接入文件列表")
    public ApiPageResponse<OriginFileInfo> page(@Valid OriginFilePageRequest request,BindingResult bindingResult) {
        startPage(request,OriginFile.class);
        List<OriginFileInfo> originFileList = originFileService.list(request);
        return ApiPageResponse.ok(originFileList,page);
    }



}
