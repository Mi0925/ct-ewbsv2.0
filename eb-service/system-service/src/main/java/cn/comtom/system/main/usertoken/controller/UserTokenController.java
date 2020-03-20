package cn.comtom.system.main.usertoken.controller;


import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.domain.system.usertoken.request.UserTokenAddRequest;
import cn.comtom.domain.system.usertoken.request.UserTokenUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.usertoken.entity.dbo.UserToken;
import cn.comtom.system.main.usertoken.service.IUserTokenService;
import cn.comtom.tools.response.ApiEntityResponse;
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

@RestController
@RequestMapping("/system/userToken")
@Api(tags = "用户令牌管理接口")
@Slf4j
public class UserTokenController extends BaseController {

    @Autowired
    private IUserTokenService userTokenService;


    @PostMapping("/save")
    @ApiOperation(value = "保存用户令牌信息", notes = "保存用户令牌信息")
    public ApiEntityResponse<UserTokenInfo> save(@RequestBody UserTokenAddRequest request, BindingResult result) {
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        UserToken userToken = new UserToken();
        String tokenId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,userToken);
        }
        userToken.setTokenId(tokenId);
        userTokenService.save(userToken);
        BeanUtils.copyProperties(userToken,userTokenInfo);
        return ApiEntityResponse.ok(userTokenInfo);
    }

    @GetMapping("/{tokenId}")
    @ApiOperation(value = "根据ID查询用户令牌信息", notes = "根据ID查询用户令牌信息")
    public ApiEntityResponse<UserTokenInfo> getById(@PathVariable(name = "tokenId") String tokenId) {
        if(log.isDebugEnabled()){
            log.debug("get user token info by token id  tokenId=[{}]",tokenId);
        }
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        UserToken userToken = userTokenService.selectById(tokenId);
        if(userToken == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        BeanUtils.copyProperties(userToken,userTokenInfo);
        return ApiEntityResponse.ok(userTokenInfo);
    }

    @GetMapping("/getToken")
    @ApiOperation(value = "获取用户令牌", notes = "获取用户令牌")
    public ApiEntityResponse<UserTokenInfo> getToken(@RequestParam(name = "userId") String userId,@RequestParam(name = "clientType") String clientType) {
        if(log.isDebugEnabled()){
            log.debug("get user token   userId=[{}]  clientType=[{}] ",userId,clientType);
        }
        UserTokenInfo userTokenInfo = userTokenService.getToken(userId,clientType);
        if(userTokenInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(userTokenInfo);
    }
    @GetMapping("/getByAccessToken")
    @ApiOperation(value = "根据访问令牌获取用户令牌信息", notes = "根据访问令牌获取用户令牌信息")
    public ApiEntityResponse<UserTokenInfo> getByAccessToken(@RequestParam(name = "accessToken") String accessToken) {
        if(log.isDebugEnabled()){
            log.debug("get user token by accessToken  accessToken=[{}] ",accessToken);
        }
        UserTokenInfo userTokenInfo = userTokenService.getByAccessToken(accessToken);
        if(userTokenInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(userTokenInfo);
    }

    @GetMapping("/getByRefreshToken")
    @ApiOperation(value = "根据刷新令牌获取用户令牌信息", notes = "根据刷新令牌获取用户令牌信息")
    public ApiEntityResponse<UserTokenInfo> getByRefreshToken(@RequestParam(name = "refreshToken") String refreshToken) {
        if(log.isDebugEnabled()){
            log.debug("get user token by refreshToken  refreshToken=[{}] ",refreshToken);
        }
        UserTokenInfo userTokenInfo = userTokenService.getByRefreshToken(refreshToken);
        if(userTokenInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(userTokenInfo);
    }

    @DeleteMapping("/delete/{tokenId}")
    @ApiOperation(value = "删除令牌", notes = "删除令牌")
    public ApiResponse delete(@PathVariable(name = "tokenId") String tokenId) {
        if(log.isDebugEnabled()){
            log.debug("delete user token by token id  tokenId=[{}]",tokenId);
        }
        userTokenService.deleteById(tokenId);
        return ApiResponse.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新令牌", notes = "更新令牌")
    public ApiResponse update(@RequestBody @Valid UserTokenUpdateRequest request,BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("update user token by token id  request=[{}]",request);
        }
        UserToken userToken = new UserToken();
        BeanUtils.copyProperties(request,userToken);
        userTokenService.update(userToken);
        return ApiResponse.ok();
    }




}
