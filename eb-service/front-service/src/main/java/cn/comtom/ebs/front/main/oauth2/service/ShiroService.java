package cn.comtom.ebs.front.main.oauth2.service;

import cn.comtom.ebs.front.main.oauth2.model.TokenInfo;
import cn.comtom.ebs.front.main.oauth2.controller.request.OauthRequest;
import cn.comtom.ebs.front.main.oauth2.controller.request.RefreshTokenRequest;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ShiroService {

    Set<String> getUserRoles(String userId);

    Set<String> getPermCodesByRoleIds(Collection<String> roleIds);

    Map<String,Object> oauth(OauthRequest request);

    TokenInfo refreshToken(RefreshTokenRequest request);

    boolean logout(TokenInfo tokenInfo);

    Set<String> getPermCodesByUserId(String userId);

}
