package cn.comtom.ebs.front.main.oauth2.service;

import cn.comtom.domain.system.usertoken.info.UserTokenInfo;

import java.time.Duration;
import java.util.List;

public interface UserTokenService {

    /**
     * 访问令牌有效时长
     */
    Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);

    /**
     * 刷新令牌有效时长
     */
    Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);


    UserTokenInfo merge(UserTokenInfo userToken);


    boolean delete(UserTokenInfo userToken);


    UserTokenInfo getToken(String userId, String clientType);

    List<UserTokenInfo> listUserToken(String userId);

    List<UserTokenInfo> listUserTokenExcludeType(String userId, String clientType);



    UserTokenInfo getAccessToken(String accessToken);

    UserTokenInfo getRefreshToken(String refreshToken);


    /**
     * 更新过期时间
     * @param userToken
     * @return
     */

    boolean updateAccessExpireTime(UserTokenInfo userToken);

}
