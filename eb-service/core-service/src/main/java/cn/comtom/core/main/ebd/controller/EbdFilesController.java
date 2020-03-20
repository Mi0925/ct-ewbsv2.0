package cn.comtom.core.main.ebd.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebd.entity.dbo.EbdFiles;
import cn.comtom.core.main.ebd.service.IEbdFilesService;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.request.EbdFilesAddBatchRequest;
import cn.comtom.domain.core.ebd.request.EbdFilesAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core/ebd")
@Api(tags = "业务数据包关联文件信息")
@Slf4j
public class EbdFilesController extends BaseController {

    @Autowired
    private IEbdFilesService ebdFilesService;


    @PostMapping("/files/save")
    @ApiOperation(value = "保存业务数据包关联文件信息", notes = "保存业务数据包关联文件信息")
    public ApiEntityResponse<EbdFilesInfo> save(@RequestBody @Valid EbdFilesAddRequest request, BindingResult bindResult) {
        EbdFiles ebdFiles = new EbdFiles();
        BeanUtils.copyProperties(request,ebdFiles);
        String id = UUIDGenerator.getUUID();
        ebdFiles.setId(id);
        ebdFilesService.save(ebdFiles);
        EbdFilesInfo info = new EbdFilesInfo();
        BeanUtils.copyProperties(ebdFiles,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/files/saveList")
    @ApiOperation(value = "批量保存业务数据包关联文件信息", notes = "批量保存业务数据包关联文件信息")
    public ApiResponse saveList(@RequestBody @Valid EbdFilesAddBatchRequest request, BindingResult bindResult) {
        List<EbdFiles> ebdFilesList = new ArrayList<>();
        if(request.getEbdFilesInfoList() != null && !request.getEbdFilesInfoList().isEmpty()){
            List<EbdFilesInfo> infoList = request.getEbdFilesInfoList();
            infoList.forEach(ebdFilesInfo -> {
                EbdFiles file = new EbdFiles();
                BeanUtils.copyProperties(ebdFilesInfo,file);
                file.setId(UUIDGenerator.getUUID());
                ebdFilesList.add(file);
            });
            ebdFilesService.saveList(ebdFilesList);
        }
        return ApiResponse.ok();
    }

    @GetMapping("/files/getByEbdId")
    @ApiOperation(value = "根据ebdId查询业务数据包关联文件列表", notes = "根据ebdId查询业务数据包关联文件列表")
    public ApiListResponse<EbdFilesInfo> getByEbdId(@RequestParam(name = "ebdId") String ebdId) {
        if(log.isDebugEnabled()){
            log.debug("get ebd files by ebdId . ebdId=[{}]",ebdId);
        }
        if(StringUtils.isBlank(ebdId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbdFilesInfo> ebdFilesInfoList = ebdFilesService.getByEbdId(ebdId);
        if(ebdFilesInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebdFilesInfoList);

    }


}
