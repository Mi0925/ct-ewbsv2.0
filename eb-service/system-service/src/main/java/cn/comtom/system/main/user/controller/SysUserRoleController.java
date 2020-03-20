package cn.comtom.system.main.user.controller;


import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.user.entity.dbo.SysUserRole;
import cn.comtom.system.main.user.service.ISysUserRoleService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/userRole")
@Api(tags = "用户角色绑定关系接口")
@Slf4j
public class SysUserRoleController extends BaseController {

    @Autowired
    private ISysUserRoleService userRoleService;

    @GetMapping("/getUserRoleIds")
    @ApiOperation(value = "获取用户所有角色Id", notes = "获取用户所有角色Id")
    public ApiListResponse<String> getUserRoleIds(@RequestParam(name = "userId") String userId){
        if(log.isDebugEnabled()){
            log.debug("get all roleId by userId userId=[{}]",userId);
        }
        if(StringUtils.isBlank(userId)){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        List<String> list = userRoleService.getUserRoleIds(userId);
        if(list == null){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(list);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存用户角色绑定关系", notes = "批量保存用户角色绑定关系")
    public ApiResponse saveBatch(@RequestBody @Valid SysUserRoleAddBatchRequest request, BindingResult result) {
        if (log.isDebugEnabled()) {
            log.debug("save user role ref batch  request = [{}]", request);
        }
        userRoleService.saveBatch(request.getUserId(),request.getRoleIds());
        return ApiResponse.ok();
    }

    @DeleteMapping("/deleteByUserId")
    @ApiOperation(value = "根据用户ID删除用户角色绑定关系", notes = "根据用户ID删除用户角色绑定关系")
    public ApiEntityResponse<Integer> deleteByUserId(@RequestParam(name="userId") String userId) {
        if (log.isDebugEnabled()) {
            log.debug("delete user role ref by userId.  userId = [{}]", userId);
        }
        if(StringUtils.isBlank(userId)){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        return ApiEntityResponse.ok(userRoleService.delete(sysUserRole));
    }

    @DeleteMapping("/deleteByRoleId")
    @ApiOperation(value = "根据角色ID删除用户角色绑定关系", notes = "根据角色ID删除用户角色绑定关系")
    public ApiEntityResponse<Integer> deleteByRoleId(@RequestParam(name="roleId") String roleId) {
        if (log.isDebugEnabled()) {
            log.debug("delete user role ref by roleId.  roleId = [{}]", roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(roleId);
        return ApiEntityResponse.ok(userRoleService.delete(sysUserRole));
    }
}
