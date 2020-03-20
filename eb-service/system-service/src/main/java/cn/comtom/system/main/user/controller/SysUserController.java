package cn.comtom.system.main.user.controller;


import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.user.entity.dbo.SysUser;
import cn.comtom.system.main.user.service.ISysUserService;
import cn.comtom.system.utils.PasswordUtils;
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

@RestController
@RequestMapping("/system/users")
@Api(tags = "用户管理接口")
@Slf4j
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @GetMapping("")
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    public ApiPageResponse<SysUserInfo> getUserList(@ModelAttribute @Valid UserPageRequest pageRequest, BindingResult bResult) { ;
        startPage(pageRequest, SysUser.class);
        List<SysUserInfo> list = userService.pageList(pageRequest);
        return ApiPageResponse.ok(list,page);
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    public ApiEntityResponse<SysUserInfo> getById(@PathVariable(name = "userId") String userId) {
        SysUser sysUser = userService.selectById(userId);
        if(sysUser == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtils.copyProperties(sysUser,sysUserInfo);
        return ApiEntityResponse.ok(sysUserInfo);
    }

    @GetMapping("/getByAccount")
    @ApiOperation(value = "根据用户账号查询用户信息", notes = "根据用户账号查询用户信息")
    public ApiEntityResponse<SysUserInfo> getByAccount(@RequestParam(name = "account") String account) {
        if(log.isDebugEnabled()){
            log.debug(" get  user info by user account  account = [{}]",account);
        }
        SysUserInfo sysUserInfo = userService.getByAccount(account);
        if(sysUserInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(sysUserInfo);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存用户信息", notes = "保存用户信息")
    public ApiEntityResponse<SysUserInfo> save(@RequestBody @Valid UserAddRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("   user save  request = [{}]",request);
        }
        SysUserInfo userInfo = new SysUserInfo();
        SysUser sysUser = new SysUser();
        String userId = UUIDGenerator.getUUID();
        String salt = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,sysUser);
        sysUser.setUserId(userId);
        sysUser.setStatus(Integer.parseInt(StateDictEnum.USER_STATUS_NORMAL.getKey()));
        sysUser.setDeleteFlag(false);
        sysUser.setCreateTime(new Date());
        sysUser.setSalt(salt);
        sysUser.setPassword(PasswordUtils.getEncryptedPassword(request.getPassword(),salt));
        userService.save(sysUser);
        BeanUtils.copyProperties(sysUser,userInfo);
        return ApiEntityResponse.ok(userInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    public ApiResponse update(@RequestBody @Valid SysUserUpdateRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~]  user update  request = [{}]",request);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(request,sysUser);
        userService.update(sysUser);
        return ApiResponse.ok();
    }

    @GetMapping("/authorize")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ApiEntityResponse<SysUserInfo> authorize(@RequestParam(name = "account") String account, @RequestParam(name = "password") String password) {
        if(log.isDebugEnabled()){
            log.debug("   user authorize  userAccount=[{}] , password = [{}]",account,password);
        }
       return userService.authorize(account,password);
    }

}
