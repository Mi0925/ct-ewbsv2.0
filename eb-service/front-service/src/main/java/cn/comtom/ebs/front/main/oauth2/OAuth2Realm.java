package cn.comtom.ebs.front.main.oauth2;


import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.ebs.front.main.user.service.ISysUserService;
import cn.comtom.ebs.front.main.oauth2.service.ShiroService;
import cn.comtom.ebs.front.main.oauth2.service.UserTokenService;
import cn.comtom.tools.enums.StateDictEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * 认证
 *
 */
@Component
@Slf4j
public class OAuth2Realm extends AuthorizingRealm {


    @Autowired
    @Lazy
    private ISysUserService userService;

    @Autowired
    @Lazy
    private ShiroService shiroService;

    @Autowired
    @Lazy
    private UserTokenService userTokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserInfo user = (SysUserInfo)principals.getPrimaryPrincipal();
        String userId = user.getUserId();
        Set<String> userRoles = shiroService.getUserRoles(userId);

        //根据userId查询对应的权限项
        Set<String> permissions= shiroService.getPermCodesByRoleIds(userRoles);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(userRoles);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 认证(登录时调用)
      * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        UserTokenInfo userToken = userTokenService.getAccessToken(accessToken);

        if(userToken == null || (!userToken.valid())){
            throw new IncorrectCredentialsException("token has invalid");
        }
        String userId = userToken.getUserId();
        SysUserInfo userInfo = userService.getUserInfoById(userId);
        if(userInfo == null){
            throw new IncorrectCredentialsException("token has invalid");
        }

        if(userInfo.getDeleteFlag()){
            throw new LockedAccountException("The account has been deleted. Please contact the administrator.");
        }
        //用户被停用
        if(String.valueOf(userInfo.getStatus()).equals(StateDictEnum.USER_STATUS_DISABLE.getKey())){
            throw new LockedAccountException("The account has been suspend. Please contact the administrator.");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, accessToken, getName());
        //更新token的有效时间
        userTokenService.updateAccessExpireTime(userToken);
        //将UserToken绑定线程
     //   ThreadContext.setUserToken(userToken);
        return info;
    }
}
