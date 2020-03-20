package cn.comtom.ebs.front.main.oauth2.service.impl;

import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.domain.system.usertoken.request.UserTokenAddRequest;
import cn.comtom.domain.system.usertoken.request.UserTokenUpdateRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.main.oauth2.service.UserTokenService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@CacheConfig(cacheNames = "userToken")
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    SystemFegin systemFegin;

    @Override
    @CachePut(value = "userToken",key = "'accessToken:'+#userToken.accessToken")
    public UserTokenInfo merge(UserTokenInfo userToken) {
        UserTokenAddRequest request = new UserTokenAddRequest();
        BeanUtils.copyProperties(userToken,request);
        ApiEntityResponse<UserTokenInfo> response = systemFegin.saveUserToken(request);
        if(response.getSuccessful()){
            return response.getData();
        }
        log.error("保存用户令牌失败：错误码：[{}],错误信息：[{}]",response.getCode(),response.getMsg());
        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userToken",key = "'accessToken:'+#userToken.accessToken")
    })
    public boolean delete(UserTokenInfo userToken) {
        ApiResponse response =systemFegin.deleteUserTokenById(userToken.getTokenId());
        return response.getSuccessful();
    }

    @Override
    public UserTokenInfo getToken(String userId, String clientType) {
        ApiEntityResponse<UserTokenInfo> response = systemFegin.getToken(userId,clientType);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public List<UserTokenInfo> listUserToken(String userId){
        return null;
    }


    @Override
    public List<UserTokenInfo> listUserTokenExcludeType(String userId,String clientType) {
        return null;
    }

    @Override
  //  @Cacheable(key="'accessToken:'+#accessToken",unless ="#result==null")
    public UserTokenInfo getAccessToken(String accessToken) {
        ApiEntityResponse<UserTokenInfo> response = systemFegin.getByAccessToken(accessToken);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public UserTokenInfo getRefreshToken(String refreshToken) {
        ApiEntityResponse<UserTokenInfo> response = systemFegin.getByRefreshToken(refreshToken);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    @Caching(put = {
            @CachePut(value = "userToken",key = "'accessToken:'+#userToken.accessToken")
    })
    public boolean updateAccessExpireTime(UserTokenInfo userToken) {
        /**
         * 定期更新用户令牌过期时间到数据库
         */
        if(userToken.getAccessExpireTime().getTime()-System.currentTimeMillis()< TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_DURATION.toMillis()/3)){
            Calendar accessCalendar = Calendar.getInstance();
            accessCalendar.add(Calendar.MINUTE,(int)ACCESS_TOKEN_DURATION.toMinutes());
            Date accessExpireTime = accessCalendar.getTime();
            userToken.setAccessExpireTime(accessExpireTime);
            UserTokenUpdateRequest request = new UserTokenUpdateRequest();
            BeanUtils.copyProperties(userToken,request);
            ApiResponse response = systemFegin.updateUserToken(request);
            return  response.getSuccessful();
        }
        return false;
    }
}
