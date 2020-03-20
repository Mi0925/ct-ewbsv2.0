package cn.comtom.ebs.front.main.user.controller;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.user.service.ISysUserService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/safeRest/user")
@Api(tags = "用户管理", description = "用户管理")
public class SysUserController extends AuthController {

    @Autowired
    private ISysUserService userService;

    @GetMapping("/getByUserId")
    @ApiOperation(value = "根据userId查询用户信息", notes = "根据userId查询用户信息")
    @RequiresPermissions(Permissions.SYS_USER_VIEW)
    public ApiEntityResponse<SysUserInfo> getUserInfoById(@RequestParam(name = "userId") String userId){
        if(log.isDebugEnabled()){
            log.debug("[!~] query userInfo by userId . userId=[{}]",userId);
        }
        return ApiEntityResponse.ok(userService.getUserInfoById(userId));
    }

    @GetMapping("page")
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    @RequiresPermissions(Permissions.SYS_USER_VIEW)
    public ApiPageResponse<SysUserInfo> getUserList(@Valid UserPageRequest pageRequest, BindingResult bResult) {
        ApiPageResponse<SysUserInfo> list = userService.page(pageRequest);
        return list;
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation(value = "更新用户", notes = "更新用户")
    @RequiresPermissions(Permissions.SYS_USER_EDIT)
    public ApiResponse updateUserStatus(@RequestBody @Valid SysUserUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update user info  . request=[{}]",request);
        }
        userService.updateUserInfo(request);
        return ApiResponse.ok();
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存用户", notes = "保存用户")
    @RequiresPermissions(Permissions.SYS_USER_ADD)
    public ApiEntityResponse<SysUserInfo> save(@RequestBody @Valid UserAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save user info . request=[{}]",request);
        }
        request.setCreaterId(getUserId());
        SysUserInfo sysUserInfo = userService.getByAccount(request.getAccount());
        if(sysUserInfo != null){
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.USER_ACCOUNT_EXISTS);
        }
        sysUserInfo = userService.save(request);
        if(sysUserInfo == null){
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.USER_SAVE_FAILURE);
        }
        return ApiEntityResponse.ok(sysUserInfo);
    }

}
