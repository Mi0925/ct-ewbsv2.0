package cn.comtom.system.main.usertoken.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.usertoken.entity.dbo.UserToken;
import cn.comtom.system.main.usertoken.mapper.UserTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserTokenDao extends BaseDao<UserToken,String> {

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Override
    public SystemMapper<UserToken, String> getMapper() {
        return userTokenMapper;
    }


    public UserToken getToken(String userId, String clientType) {
        UserToken userToken = new UserToken();
        userToken.setClientType(clientType);
        userToken.setUserId(userId);
        return userTokenMapper.selectOne(userToken);
    }

    public UserToken getByAccessToken(String accessToken) {
        UserToken userToken = new UserToken();
        userToken.setAccessToken(accessToken);
        return userTokenMapper.selectOne(userToken);
    }

    public UserToken getByRefreshToken(String refreshToken) {
        UserToken userToken = new UserToken();
        userToken.setRefreshToken(refreshToken);
        return userTokenMapper.selectOne(userToken);
    }
}
