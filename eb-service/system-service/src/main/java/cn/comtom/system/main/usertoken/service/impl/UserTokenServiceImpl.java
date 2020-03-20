package cn.comtom.system.main.usertoken.service.impl;

import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.usertoken.dao.UserTokenDao;
import cn.comtom.system.main.usertoken.entity.dbo.UserToken;
import cn.comtom.system.main.usertoken.service.IUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 * @author guomao
 * @Date 2018-10-29 9:30
 */
@Slf4j
@Service
public class UserTokenServiceImpl extends BaseServiceImpl<UserToken,String> implements IUserTokenService {

    @Autowired
    private UserTokenDao userTokenDao;


    @Override
    public BaseDao<UserToken, String> getDao() {
        return userTokenDao;
    }


    @Override
    public UserTokenInfo getToken(String userId, String clientType) {
        UserToken userToken = userTokenDao.getToken(userId,clientType);
        if(userToken == null){
            return null;
        }
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        BeanUtils.copyProperties(userToken,userTokenInfo);
        return userTokenInfo;
    }

    @Override
    public UserTokenInfo getByAccessToken(String accessToken) {
        UserToken userToken = userTokenDao.getByAccessToken(accessToken);
        if(userToken == null){
            return null;
        }
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        BeanUtils.copyProperties(userToken,userTokenInfo);
        return userTokenInfo;
    }

    @Override
    public UserTokenInfo getByRefreshToken(String refreshToken) {
        UserToken userToken = userTokenDao.getByRefreshToken(refreshToken);
        if(userToken == null){
            return null;
        }
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        BeanUtils.copyProperties(userToken,userTokenInfo);
        return userTokenInfo;
    }

}
