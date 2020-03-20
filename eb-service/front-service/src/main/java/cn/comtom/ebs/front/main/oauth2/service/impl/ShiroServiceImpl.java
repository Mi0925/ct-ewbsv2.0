package cn.comtom.ebs.front.main.oauth2.service.impl;

import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.ebs.front.main.oauth2.TokenGenerator;
import cn.comtom.ebs.front.main.oauth2.controller.request.OauthRequest;
import cn.comtom.ebs.front.main.oauth2.controller.request.RefreshTokenRequest;
import cn.comtom.ebs.front.main.oauth2.model.TokenInfo;
import cn.comtom.ebs.front.main.oauth2.service.ShiroService;
import cn.comtom.ebs.front.main.oauth2.service.UserTokenService;
import cn.comtom.ebs.front.main.resource.service.ISysResourceService;
import cn.comtom.ebs.front.main.role.service.ISysRoleResourceService;
import cn.comtom.ebs.front.main.user.service.ISysUserRoleService;
import cn.comtom.ebs.front.main.user.service.ISysUserService;
import cn.comtom.tools.response.ApiEntityResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames="shiro")
@Slf4j
public class ShiroServiceImpl implements ShiroService {


    @Autowired
    ISysUserService userService;

    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Autowired
    ISysRoleResourceService sysRoleResourceService;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    ISysResourceService sysResourceService;

    @Override
    public Set<String> getUserRoles(String userId){
        return sysUserRoleService.getUserRoleIds(userId);
    }



    @Override
    public Set<String> getPermCodesByRoleIds(Collection<String> roleIds) {
        Set<String> permCodes = new HashSet<>();
        List<String> rIds = new ArrayList(roleIds);
        List<SysRoleResourceInfo> sysRoleResourceInfoList = sysRoleResourceService.getSysRoleResourceByRoleIds(rIds);
        log.info("getPermCodesByRoleIds,sysRoleResourceInfoList:{}", JSON.toJSONString(sysRoleResourceInfoList));
        List<String> resourceIds = Optional.ofNullable(sysRoleResourceInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(SysRoleResourceInfo::getResourceId)
                .collect(Collectors.toList());

        if(!resourceIds.isEmpty()){
            List<SysResourceInfo> sysResourceInfoList = sysResourceService.getSysResourceByIds(resourceIds);
            log.info("getPermCodesByRoleIds,sysResourceInfoList:{}", JSON.toJSONString(sysResourceInfoList));
            permCodes = Optional.ofNullable(sysResourceInfoList).orElse(Collections.emptyList()).stream()
                    .filter(Objects::nonNull)
                    .map(SysResourceInfo::getPerms)
                    .collect(Collectors.toSet());
        }
        return permCodes;
    }

    @Override
    public Map<String,Object> oauth(OauthRequest request) {
        Map<String , Object> resultMap = new HashMap<>();
        ApiEntityResponse<SysUserInfo> response = userService.login(request.getUsername(), request.getPassword());
        if(!response.getSuccessful()){
            resultMap.put("msg",response.getMsg());
            return resultMap;
        }
        SysUserInfo userInfo = response.getData();
        if(userInfo == null ){
            return null;
        }
        String userId = userInfo.getUserId();
        String clientType = request.getClientType();
        UserTokenInfo token = userTokenService.getToken(userId, clientType);
        if(token != null){
            userTokenService.delete(token);
        }
        TokenInfo tokenInfo = createToken(userInfo.getUserId(),request.getClientType());
        resultMap.put("tokenInfo",tokenInfo);
        return resultMap;
    }

    private TokenInfo createToken(String userId,String clientType){
        //根据本地时间构造token失效时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime accessTime = localDateTime.plusMinutes(UserTokenService.ACCESS_TOKEN_DURATION.toMinutes());
        Date accessExpireTime = Date.from(accessTime.atZone(ZoneId.systemDefault()).toInstant());

        //创建token实体类
        UserTokenInfo userToken = new UserTokenInfo();
        userToken.setUserId(userId);
        userToken.setClientType(clientType);
        //生成token并设置失效时间
        String accessToken = TokenGenerator.generateValue();
        userToken.setAccessToken(accessToken);
        userToken.setAccessExpireTime(accessExpireTime);

        String refreshToken = TokenGenerator.generateValue();
        userToken.setRefreshToken(refreshToken);
        //根据本地时间构造刷新token失效时间
        LocalDateTime refreshTime = localDateTime.plusDays(UserTokenService.REFRESH_TOKEN_DURATION.toDays());
        Date refreshExpireTime = Date.from(refreshTime.atZone(ZoneId.systemDefault()).toInstant());
        userToken.setRefreshExpireTime(refreshExpireTime);
        userTokenService.merge(userToken);
        TokenInfo tokenInfo = new TokenInfo();
        BeanUtils.copyProperties(userToken,tokenInfo);
        return tokenInfo;
    }

    @Override
    public TokenInfo refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        UserTokenInfo userToken = userTokenService.getRefreshToken(refreshToken);
        if(userToken == null ){
            return null;
        }
        if(userToken.getRefreshExpireTime().before(new Date())){
            userTokenService.delete(userToken);
            return null;
        }
        String userId = request.getUserId();
        if(!userToken.getUserId().equals(userId)){
            return null;
        }
        userTokenService.delete(userToken);
        return createToken(userId,userToken.getClientType());
    }

    @Override
    public boolean logout(TokenInfo tokenInfo) {
        UserTokenInfo userToken = userTokenService.getAccessToken(tokenInfo.getAccessToken());
        if(userToken!=null){
            userTokenService.delete(userToken);
        }
        return true;
    }

    @Override
    public Set<String> getPermCodesByUserId(String userId) {
        Set<String> userRoleIds = getUserRoles(userId);
        log.info("getPermCodesByUserId:userId:{},result:{}", userId, JSON.toJSONString(userRoleIds));
        if(userRoleIds == null || userRoleIds.isEmpty()){
            return new HashSet<>();
        }
        return  getPermCodesByRoleIds(userRoleIds);
    }


}
