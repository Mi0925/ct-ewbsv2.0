package cn.comtom.ebs.front.main.oauth2.controller;


import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.common.OpenRest;
import cn.comtom.ebs.front.common.StandardRedis;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.BaseController;
import cn.comtom.ebs.front.main.oauth2.ErrorEnum;
import cn.comtom.ebs.front.main.oauth2.controller.request.OauthRequest;
import cn.comtom.ebs.front.main.oauth2.controller.request.RefreshTokenRequest;
import cn.comtom.ebs.front.main.oauth2.model.TokenInfo;
import cn.comtom.ebs.front.main.oauth2.service.ShiroService;
import cn.comtom.ebs.front.main.oauth2.service.UserTokenService;
import cn.comtom.ebs.front.main.user.service.ISysUserService;
import cn.comtom.ebs.front.utils.ShiroUtils;
import cn.comtom.tools.constants.RedisKeyConstants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/openRest/oauth2")
@Api(tags = "令牌授权", description = "支持获取令牌及更新令牌")
@OpenRest
public class Oauth2Controller extends BaseController {
    @Autowired
    private ShiroService shiroService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private StandardRedis standardRedis;
/*
    @Autowired
    private ISysParamsService sysParamService;*/

    private String getSessionId() {
        String sessionId = ShiroUtils.getSession().getId().toString();
        log.debug("SessionId:{}", sessionId);
        return sessionId;
    }

/*    @GetMapping("/qrCode")
    @ApiOperation(value = "查询二维码信息", notes = "查询二维码信息")
    public ApiEntityResponse<SysParamsInfo> qrCode() {
        SysParamsInfo model = new SysParamsInfo();
        model.setCode("qrCode");
        List<SysParamEntity> list = sysParamService.queryListSelective(model);
        SysParamEntity sysparam = CollectionUtils.isNotEmpty(list) && null != list.get(0) ? list.get(0) : new SysParamEntity();
        return ApiEntityResponse.ok(sysparam);
    }*/

    @ApiOperation(value = "web端获取验证码", notes = "web端获取验证码")
    @RequestMapping(value = "captcha.jpg", method = RequestMethod.GET)
    public void captcha(HttpServletResponse response,HttpServletRequest request) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        log.debug("request host : "+ request.getRemoteHost());
        String captcha = captchaProducer.createText();
        standardRedis.setCaptcha(RedisKeyConstants.CAPTCHA_KEY_PREFIX.concat(request.getRemoteHost()), captcha);
        //保存到shiro session
//        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
        log.info(captcha);
        BufferedImage image = captchaProducer.createImage(captcha);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);

    }

    @ApiOperation(value = "验证验证码", notes = "验证验证码")
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.GET)
    public ApiResponse verifyCaptcha(@RequestParam(name = "captcha") String captcha,HttpServletRequest request) {
        log.debug("request ip : "+ request.getRemoteHost());
        String oldCaptcha = standardRedis.getCaptcha(RedisKeyConstants.CAPTCHA_KEY_PREFIX.concat(request.getRemoteHost()));
        if(StringUtils.isBlank(captcha)){
            return ApiResponseBuilder.buildError(FrontErrorEnum.CAPTCHA_EMPTY);
        }
        if(StringUtils.isBlank(oldCaptcha)){
            return ApiResponseBuilder.buildError(FrontErrorEnum.CAPTCHA_INVALID);
        }
        if(!oldCaptcha.equals(captcha)){
            return ApiResponseBuilder.buildError(FrontErrorEnum.CAPTCHA_VERIFY_FAILURE);
        }
        return ApiResponse.ok();
    }

    @ApiOperation(value = "验证token", notes = "验证token")
    @RequestMapping(value = "/validate/token", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse validateToken(@RequestParam(value = "token") String token) {
        UserTokenInfo userToken = userTokenService.getAccessToken(token);
        if (userToken != null && userToken.valid()) {
            return ApiResponse.ok();
        }
        return ApiResponseBuilder.buildError(ErrorEnum.INVALID_TOKEN);
    }


    @ApiOperation(value = "获取令牌")
    @PostMapping(value = "/authorize")
    public ApiEntityResponse<TokenInfo> authorize(@RequestBody @Valid OauthRequest request, BindingResult bResult) {
        //生成token，并保存到数据库
        Map<String,Object> resultMap = shiroService.oauth(request);
        String errorMsg = Optional.ofNullable(resultMap)
                .filter(map->map.get("msg") != null)
                .map(map -> map.get("msg").toString())
                .orElseGet(String::new);
        if(StringUtils.isNotBlank(errorMsg)){
            return ApiEntityResponse.error(FrontErrorEnum.USER_AUTHORIZE_FAILURE.getCode(),errorMsg);
        }
        TokenInfo tokenInfo = Optional.ofNullable(resultMap)
                .filter(map -> map.get("tokenInfo") instanceof TokenInfo)
                .map(map -> (TokenInfo)map.get("tokenInfo"))
                .orElseGet(TokenInfo::new);
        //记录登录日志
     //   UserInfo userInfo = userService.getUserInfo(tokenInfo.getUserId());
     //   LogRunner.me().executeLog(LogTaskWrap.loginLog(userInfo, httpServletRequest, tokenInfo.getClientType()));
        return ApiEntityResponse.ok(tokenInfo);
    }

    @ApiOperation(value = "更新令牌")
    @RequestMapping(value = "/refreshToken", method = {RequestMethod.POST})
    public ApiEntityResponse<TokenInfo> refreshToken(@RequestBody @Valid RefreshTokenRequest request, BindingResult bResult) {
        TokenInfo tokenInfo = shiroService.refreshToken(request);
        if (tokenInfo == null) {
            return ApiResponseBuilder.buildEntityError(ErrorEnum.INVALID_TOKEN);
        }
        return ApiEntityResponse.ok(tokenInfo);
    }

    @RequiresAuthentication
    @ApiOperation(value = "使令牌失效")
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "accessToken", required = true)})
    public ApiResponse invalidateToken(@RequestHeader String token, HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(token);
        boolean isOK = shiroService.logout(tokenInfo);
        if (isOK) {
            //记录登出日志
         //   LogRunner.me().executeLog(LogTaskWrap.exitLog(userEntity, httpServletRequest, tokenInfo.getClientType()));
            return ApiResponse.ok();
        }
        SecurityUtils.getSubject().logout();
        return ApiResponseBuilder.buildError(ErrorEnum.INVALID_TOKEN);
    }

    @RequiresAuthentication
    @GetMapping(value = "checkPermission")
    public ApiEntityResponse<Map<String, Boolean>> checkPermission(@RequestParam(name = "permissionCodes") String[] permissionCodes) {
        boolean[] permitted = SecurityUtils.getSecurityManager().isPermitted(SecurityUtils.getSubject().getPrincipals(), permissionCodes);

        Map<String, Boolean> permissions = new HashMap<>();
        for (int i = 0; i < permissionCodes.length; i++) {
            permissions.put(permissionCodes[i], permitted[i]);
        }
        return ApiEntityResponse.ok(permissions);
    }


    @ApiOperation(value = "通过系统参数编码获取系统参数")
    @RequestMapping(value = "/getSysParamsInfo", method = {RequestMethod.GET})
    public ApiEntityResponse<SysParamsInfo> getSysParam(@RequestParam(name = "paramKey") String paramKey) {
        if (StringUtils.isBlank(paramKey )) {
            return ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(paramKey);
        return ApiEntityResponse.ok(sysParamsInfo);
    }

}
