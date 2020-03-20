package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.Access;
import cn.comtom.core.main.access.service.IAccessAreaService;
import cn.comtom.core.main.access.service.IAccessFileService;
import cn.comtom.core.main.access.service.IAccessService;
import cn.comtom.core.main.access.service.IAccessStrategyService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.*;
import cn.comtom.domain.core.access.request.AccessAuditRequest;
import cn.comtom.domain.core.access.request.AccessInfoAddRequest;
import cn.comtom.domain.core.access.request.AccessInfoPageRequest;
import cn.comtom.domain.core.access.request.AccessInfoUpdateRequest;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
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
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core/access/info")
@Api(tags = "信息接入")
public class AccessController extends BaseController {

    @Autowired
    private IAccessService accessService;

    @Autowired
    private IAccessStrategyService accessStrategyService;

    @Autowired
    private IAccessFileService accessFileService;

    @Autowired
    private IAccessAreaService accessAreaService;

    @GetMapping("/page")
    @ApiOperation(value = "查询信息区域编码列表", notes = "查询信息区域编码列表")
    public ApiPageResponse<AccessInfo> page(@Valid AccessInfoPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("enter access query by page req=[{}]",request);
        }
        startPage(request,Access.class);
        List<AccessInfo> infoList = accessService.list(request);
        return ApiPageResponse.ok(infoList,page);
    }

    @GetMapping("/{infoId}")
    @ApiOperation(value = "根据ID查询信息详情", notes = "根据ID查询信息详情")
    public ApiEntityResponse<AccessInfo> getById(@PathVariable(name = "infoId") String infoId){
        if(log.isDebugEnabled()){
            log.debug("enter access query by  infoId=[{}]",infoId);
        }
        Access access = accessService.selectById(infoId);
        if(access == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessInfo info = new AccessInfo();
        BeanUtils.copyProperties(access,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入基本信息", notes = "保存接入基本信息")
    public ApiEntityResponse<AccessInfo> save(@RequestBody @Valid AccessInfoAddRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter Access save request=[{}]",request);
        }
        Access access = new Access();
        String id = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,access);
        access.setInfoId(id);
        access.setCreateTime(new Date());
        access.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        accessService.save(access);
        AccessInfo info = new AccessInfo();
        BeanUtils.copyProperties(access,info);
        return ApiEntityResponse.ok(info);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新接入基本信息", notes = "更新接入基本信息")
    public ApiResponse update(@RequestBody @Valid AccessInfoUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter access update request=[{}]",request);
        }
        Access access = new Access();
        BeanUtils.copyProperties(request,access);
        accessService.update(access);
        return ApiResponse.ok();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "接入基本信息审核", notes = "接入基本信息审核")
    public ApiResponse audit(@RequestBody @Valid AccessAuditRequest request, BindingResult result){
        Access access = new Access();
        BeanUtils.copyProperties(request,access);
        access.setAuditTime(new Date());
        accessService.update(access);
        return ApiResponse.ok();
    }

    @GetMapping("/getInfoAll")
    @ApiOperation(value = "根据ID查询信息详情", notes = "根据ID查询信息详情")
    public ApiEntityResponse<AccessInfoAll> getInfoAll(@RequestParam(name = "infoId") String infoId){
        if(log.isDebugEnabled()){
            log.debug("enter access query all info by  infoId=[{}]",infoId);
        }
        AccessInfoAll infoAll = new AccessInfoAll();
        Access access = accessService.selectById(infoId);
        if(access != null){
            BeanUtils.copyProperties(access,infoAll);
        }
        List<AccessFileInfo> accessFileList = accessFileService.getByInfoId(infoId);
        infoAll.setAccessFileInfoList(accessFileList);
        List<AccessAreaInfo> accessAreaInfoList = accessAreaService.getByInfoId(infoId);
        infoAll.setAccessAreaInfoList(accessAreaInfoList);
        List<AccessStrategyAll> accessStrategyAllList = accessStrategyService.getInfoAllByInfoId(infoId);
        infoAll.setAccessStrategyList(accessStrategyAllList);
        return ApiEntityResponse.ok(infoAll);
    }

}
