package cn.comtom.ebs.front.main.role.controller;

import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRoleAddRequest;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.domain.system.role.request.SysRoleUpdateRequest;
import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.role.service.ISysRoleResourceService;
import cn.comtom.ebs.front.main.role.service.ISysRoleService;
import cn.comtom.ebs.front.main.user.service.ISysUserRoleService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/safeRest/role")
@Api(tags = "角色管理", description = "角色管理")
@AuthRest
public class SysRoleController extends AuthController {


    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleResourceService sysRoleResourceService;


    @GetMapping("/page")
    @ApiOperation(value = "分页角色列表", notes = "分页角色列表")
    @RequiresPermissions(Permissions.SYS_ROLE_VIEW)
    public ApiPageResponse<SysRoleInfo> page(@ModelAttribute @Valid SysRolePageRequest request, BindingResult result) {
        return sysRoleService.page(request);
    }

    @GetMapping("/getRoles")
    @ApiOperation(value = "查询所有角色列表", notes = "查询所有角色列表")
    @RequiresPermissions(Permissions.SYS_ROLE_VIEW)
    public ApiPageResponse<SysRoleInfo> getRoles() {
        if(log.isDebugEnabled()){
            log.debug("[!~] get all roles .");
        }
        SysRolePageRequest request = new SysRolePageRequest();
        request.setLimit(Constants.MAX_SQL_ROWS);
        return sysRoleService.page(request);
    }

    @GetMapping("/getUserRoleIds")
    @ApiOperation(value = "查询所有角色列表", notes = "查询所有角色列表")
    @RequiresPermissions(Permissions.SYS_ROLE_VIEW)
    public ApiListResponse<String> getUserRoleIds(@RequestParam(name = "userId") String userId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get user roles by userId .userId =[{}] ",userId);
        }
        Set<String> set = sysUserRoleService.getUserRoleIds(userId);
        return ApiListResponse.ok( new ArrayList<>(set));
    }

    @GetMapping("/{roleId}")
    @ApiOperation(value = "根据ID查询角色信息", notes = "根据ID查询角色信息")
    @RequiresPermissions(Permissions.SYS_ROLE_VIEW)
    public ApiEntityResponse<SysRoleInfo> getByRoleId(@PathVariable(name = "roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get role info by roleId .roleId =[{}] ",roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        return sysRoleService.getRoleInfoById(roleId);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存角色", notes = "保存角色")
    @RequiresPermissions(Permissions.SYS_ROLE_ADD)
    public ApiEntityResponse<SysRoleInfo> saveRole(@RequestBody @Valid SysRoleAddRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save sysRole  .request =[{}] ",request);
        }
        return sysRoleService.saveSysRoleInfo(request);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新角色", notes = "更新角色")
    @RequiresPermissions(Permissions.SYS_ROLE_EDIT)
    public ApiResponse updateRole(@RequestBody @Valid SysRoleUpdateRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update sysRole  .request =[{}] ",request);
        }
        return sysRoleService.updateSysRoleInfo(request);
    }

    @PostMapping("/refRoles")
    @ApiOperation(value = "批量绑定用户角色", notes = "批量绑定用户角色")
    @RequiresPermissions(Permissions.SYS_USER_ROLE_REF)
    public ApiResponse refRoles(@RequestBody @Valid SysUserRoleAddBatchRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] user role ref .request =[{}] ",request);
        }
        if(sysUserRoleService.deleteUserRoleByUserId(request.getUserId())){
            sysUserRoleService.saveUserRoleRefBatch(request);
        }else{
            return ApiResponseBuilder.buildError(FrontErrorEnum.USER_ROLE_ADD_FAILURE);
        }
        return ApiResponse.ok();
    }

    @GetMapping("/getRoleResources")
    @ApiOperation(value = "根据角色ID查询所有该角色拥有的系统资源", notes = "根据角色ID查询所有该角色拥有的系统资源")
    @RequiresPermissions(Permissions.SYS_ROLE_VIEW)
    public ApiListResponse<SysRoleResourceInfo> getRoleResources(@RequestParam(name = "roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get role resources by roleId .roleId =[{}] ",roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        return sysRoleResourceService.getRoleResourcesByRoleId(roleId);
    }

    @DeleteMapping("/delete/{roleId}")
    @ApiOperation(value = "根据角色ID删除角色并删除所有该角色拥有的系统资源", notes = "根据角色ID删除角色并删除所有该角色拥有的系统资源")
    @RequiresPermissions(Permissions.SYS_ROLE_DELETE)
    public ApiResponse deleteRole(@PathVariable(name = "roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete role  by roleId .roleId =[{}] ",roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        //删除角色信息，同时删除角色资源绑定关系和用户角色绑定关系
        if(sysRoleService.deleteRoleInfoById(roleId)){
            //删除角色资源绑定关系
            if(!sysRoleResourceService.deleteRoleResourceByRoleId(roleId)){
                log.error(FrontErrorEnum.DELETE_ROLE_RESOURCE_FAILURE.getMsg());
            }
            //删除用户角色绑定关系
            if(!sysUserRoleService.deleteUserRoleByRoleId(roleId)){
                log.error(FrontErrorEnum.DELETE_USER_ROLE_FAILURE.getMsg());
            }
        }else{
            return ApiResponseBuilder.buildError(FrontErrorEnum.DELETE_ROLE_FAILURE);
        }
        return ApiResponse.ok();
    }
}
