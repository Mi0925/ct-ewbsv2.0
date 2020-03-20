package cn.comtom.ebs.front.main.oauth2;


import cn.comtom.domain.system.usertoken.info.UserTokenInfo;

public class ThreadContext {
    private static final ThreadLocal<UserTokenInfo> USER_TOKEN = new ThreadLocal<>();

    public static void setUserToken(UserTokenInfo userToken){
        USER_TOKEN.set(userToken);
    }

    public static UserTokenInfo getUserToken(){
        return USER_TOKEN.get();
    }

    public static void removeUserToken(){
        USER_TOKEN.remove();
    }
}
