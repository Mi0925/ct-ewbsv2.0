package cn.comtom.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 服务入口方法类
 * @author wj
 */

@EnableEurekaClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableWebFlux
public class GatewayApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

