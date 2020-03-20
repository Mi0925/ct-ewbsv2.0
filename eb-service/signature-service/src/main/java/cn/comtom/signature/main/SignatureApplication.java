package cn.comtom.signature.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 服务入口方法类
 * @author wj
 */

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.comtom.signature.main.fegin"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,MultipartAutoConfiguration.class})
@EnableRetry
public class SignatureApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SignatureApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SignatureApplication.class, args);
        new CommonsMultipartResolver();
    }

    // 显示声明CommonsMultipartResolver为mutipartResolver  
    @Bean
    public MultipartResolver multipartResolver() {
    	CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    	resolver.setMaxUploadSize(30 * 1024 * 1024);
    	return resolver;
    }
}

