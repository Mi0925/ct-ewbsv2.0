package cn.comtom.ebs.front.main.params.controller;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import cn.comtom.domain.system.sysparam.request.SysParamsUpdateRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.params.service.ISysParamsService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/safeRest/sys/param")
@Api(tags = "系统参数管理", description = "系统参数管理")
@AuthRest
public class SysParamsController extends AuthController {

    @Autowired
    private ISysParamsService sysParamsService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询参数列表", notes = "分页查询参数列表")
    @RequiresPermissions(Permissions.SYS_PARAM_VIEW)
    public ApiPageResponse<SysParamsInfo> page(@Valid SysParamPageRequest request, BindingResult result) {
        return sysParamsService.page(request);
    }

    @GetMapping("/{paramId}")
    @ApiOperation(value = "根据ID查询系统参数信息", notes = "根据ID查询系统参数信息")
    @RequiresPermissions(Permissions.SYS_PARAM_VIEW)
    public ApiEntityResponse<SysParamsInfo> getById(@PathVariable(name = "paramId") String paramId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get system param info by paramId . paramId=[{}] ",paramId);
        }
        return sysParamsService.getSysParamsInfoById(paramId);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改系统参数信息", notes = "修改系统参数信息")
    @RequiresPermissions(Permissions.SYS_PARAM_EDIT)
    public ApiResponse update(@RequestBody @Valid SysParamsUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update system param info  . request=[{}] ",request);
        }

        return sysParamsService.updateSysParamsInfo(request);
    }

    @GetMapping("/getByParamKey")
    @ApiOperation(value = "根据ID查询系统参数信息", notes = "根据ID查询系统参数信息")
    @RequiresPermissions(Permissions.SYS_PARAM_VIEW)
    public ApiEntityResponse<SysParamsInfo> getByParamKey(@RequestParam(name = "paramKey") String paramKey) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get system param info by paramKey . paramKey=[{}] ",paramKey);
        }
        return sysParamsService.getSysParamsInfoByKey(paramKey);
    }

}
