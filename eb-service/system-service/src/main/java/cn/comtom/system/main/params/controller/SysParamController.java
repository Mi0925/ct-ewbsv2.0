package cn.comtom.system.main.params.controller;


import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import cn.comtom.domain.system.sysparam.request.SysParamsUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.params.entity.dbo.SysParams;
import cn.comtom.system.main.params.service.ISysParamService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
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
@RequestMapping("/system/param")
@Api(tags = "参数管理接口")
@Slf4j
public class SysParamController extends BaseController {

    @Autowired
    private ISysParamService sysParamService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询参数列表", notes = "分页查询参数列表")
    public ApiPageResponse<SysParamsInfo> page(@ModelAttribute  @Valid SysParamPageRequest request, BindingResult result) {
        startPage(request);
        List<SysParamsInfo> list = sysParamService.pageList(request);
        ApiPageResponse<SysParamsInfo> sysParamsInfoApiPageResponse = ApiPageResponse.ok(list,page);
        return sysParamsInfoApiPageResponse;
    }

    @GetMapping("/getByKey")
    @ApiOperation(value = "根据key查询参数信息", notes = "根据key查询参数信息")
    public ApiEntityResponse<SysParamsInfo> getByKey(@RequestParam(name = "paramKey") String paramKey) {
        SysParamsInfo sysParamsInfo = sysParamService.getByKey(paramKey);
        if(sysParamsInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
            return ApiEntityResponse.ok(sysParamsInfo);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询参数信息", notes = "根据ID查询参数信息")
    public ApiEntityResponse<SysParamsInfo> getById(@PathVariable(name = "id") String id) {
        SysParams sysParams = sysParamService.selectById(id);
        if(sysParams == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        SysParamsInfo sysParamsInfo = new SysParamsInfo();
        BeanUtils.copyProperties(sysParams,sysParamsInfo);
        return ApiEntityResponse.ok(sysParamsInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改系统参数信息", notes = "修改系统参数信息")
    public ApiResponse update(@RequestBody SysParamsUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update system param . request=[{}]",request);
        }
        SysParams sysParams = new SysParams();
        BeanUtils.copyProperties(request,sysParams);
        sysParamService.update(sysParams);
        return ApiResponse.ok();
    }



}
