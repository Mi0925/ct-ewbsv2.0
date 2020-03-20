package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.AccessFile;
import cn.comtom.core.main.access.service.IAccessFileService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.AccessFileInfo;
import cn.comtom.domain.core.access.request.AccessFileAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core/access/file")
@Api(tags = "信息接入文件")
public class AccessFileController extends BaseController {

    @Autowired
    private IAccessFileService accessFileService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询信息接入文件", notes = "根据ID查询信息接入文件")
    public ApiEntityResponse<AccessFileInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter accessFile query by  id=[{}]",id);
        }
        AccessFile accessFile = accessFileService.selectById(id);
        if(accessFile == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessFileInfo info = new AccessFileInfo();
        BeanUtils.copyProperties(accessFile,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存信息接入文件", notes = "保存信息接入文件")
    public ApiEntityResponse<AccessFileInfo> save(@RequestBody @Valid AccessFileAddRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter AccessFile save request=[{}]",request);
        }
        AccessFile accessFile = new AccessFile();
        String id = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,accessFile);
        accessFile.setId(id);
        accessFileService.save(accessFile);
        AccessFileInfo info = new AccessFileInfo();
        BeanUtils.copyProperties(accessFile,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByInfoId")
    @ApiOperation(value = "根据信息ID查询接入文件", notes = "根据信息ID查询接入文件")
    public ApiListResponse<AccessFileInfo> getByInfoId(@RequestParam(name = "infoId") String infoId){
        if(log.isDebugEnabled()){
            log.debug("enter accessFile query by  infoId=[{}]",infoId);
        }
        List<AccessFileInfo> accessFileInfoList = accessFileService.getByInfoId(infoId);
        if(accessFileInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(accessFileInfoList);
    }

}
