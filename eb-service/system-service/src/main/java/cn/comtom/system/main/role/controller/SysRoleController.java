package cn.comtom.system.main.role.controller;


import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRoleAddRequest;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.domain.system.role.request.SysRoleUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.role.entity.dbo.SysRole;
import cn.comtom.system.main.role.service.ISysRoleService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/system/role")
@Api(tags = "角色信息管理接口")
@Slf4j
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService sysRoleService;


    @PostMapping("/save")
    @ApiOperation(value = "保存角色信息", notes = "保存角色信息")
    public ApiEntityResponse<SysRoleInfo> save(@RequestBody SysRoleAddRequest request, BindingResult result) {
        SysRole sysRole = new SysRole();
        String roleId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,sysRole);
        }
        sysRole.setId(roleId);
        sysRole.setCreateTime(new Date());
        sysRoleService.save(sysRole);
        SysRoleInfo sysRoleInfo = new SysRoleInfo();
        BeanUtils.copyProperties(sysRole,sysRoleInfo);
        return ApiEntityResponse.ok(sysRoleInfo);
    }

    @GetMapping("/{roleId}")
    @ApiOperation(value = "根据角色ID批量查询角色资源绑定关系", notes = "根据角色ID批量查询角色资源绑定关系")
    public ApiEntityResponse<SysRoleInfo> getById(@PathVariable(name = "roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get SysRole by roleId.  roleId=[{}]",roleId);
        }
        SysRole sysRole = sysRoleService.selectById(roleId);
        if (sysRole == null ) {
            return  ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        SysRoleInfo sysRoleInfo = new SysRoleInfo();
        BeanUtils.copyProperties(sysRole,sysRoleInfo);
        return ApiEntityResponse.ok(sysRoleInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询角色列表", notes = "分页查询角色列表")
    public ApiPageResponse<SysRoleInfo> page(@ModelAttribute  @Valid SysRolePageRequest request, BindingResult result) {
        startPage(request, SysRole.class);
        List<SysRoleInfo> list = sysRoleService.page(request);
        return ApiPageResponse.ok(list,page);
    }

    @DeleteMapping("/delete/{roleId}")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    public ApiResponse deleteById(@PathVariable(name="roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete SysRole by roleId.  roleId=[{}]",roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        sysRoleService.deleteById(roleId);
        return ApiResponse.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    public ApiResponse updateRoleInfo(@RequestBody @Valid SysRoleUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update SysRole .  request=[{}]",JSON.toJSONString(request));
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(request,sysRole);
        sysRoleService.update(sysRole);
        return ApiResponse.ok();
    }

}
