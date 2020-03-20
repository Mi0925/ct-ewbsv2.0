package cn.comtom.system.main.usertoken.service;


import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.usertoken.entity.dbo.UserToken;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface IUserTokenService extends BaseService<UserToken,String> {


  UserTokenInfo getToken(String userId, String clientType);

  UserTokenInfo getByAccessToken(String accessToken);

  UserTokenInfo getByRefreshToken(String refreshToken);

}
