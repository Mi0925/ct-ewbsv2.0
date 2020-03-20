package cn.comtom.ebs.front.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wjd
 * @create 2018/3/30 0030
 * @desc ${DESCRIPTION}
 **/
@Component
public class StandardRedis {
    @Autowired
    private RedisComponent redisComponent;

    Long captchaTimeout = TimeUnit.MINUTES.toSeconds(5); // 验证码存放redis 有效时间3分钟


    public void setRegCaptcha(String phone,String captcha){
        redisComponent.set(StandardRedisKeys.getRegCaptcha(phone),captcha,captchaTimeout);
    }

    public String getRegCaptcha(String phone){
        return (String) redisComponent.get(StandardRedisKeys.getRegCaptcha(phone));
    }

    public void setPwdCaptcha(String phone,String captcha){
        redisComponent.set(StandardRedisKeys.getPwdCaptcha(phone),captcha,captchaTimeout);
    }

    public String getPwdCaptcha(String phone){
        return (String) redisComponent.get(StandardRedisKeys.getPwdCaptcha(phone));
    }

    public void setCaptcha(String captchaId, String captcha){
        redisComponent.set(StandardRedisKeys.getCaptchaKey(captchaId),captcha,captchaTimeout);
    }

    public String getCaptcha(String captchaId){
        return (String) redisComponent.get(StandardRedisKeys.getCaptchaKey(captchaId));
    }

}
