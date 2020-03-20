package cn.comtom.ebs.front.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.impl.NoNoise;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wjd
 * @create 2018/5/5 0005
 * @desc 生成验证码配置
 **/
@Configuration
public class KaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
//        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL,NoNoise.class.getName());
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "125");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "50");
        properties.setProperty(Constants.KAPTCHA_SESSION_KEY, "code");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
