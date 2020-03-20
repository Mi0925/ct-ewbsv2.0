package cn.comtom.core.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 服务入口方法类
 * @author wj
 */

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.comtom.core.main.fegin"})
public class CoreApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CoreApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}

