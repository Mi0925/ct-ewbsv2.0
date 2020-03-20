package cn.comtom.ebs.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.comtom.ebs.front.fegin"})
@SpringBootApplication(scanBasePackages={"cn.comtom.ebs.front"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EbsWebApplication extends SpringBootServletInitializer{
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(EbsWebApplication.class);
        }

        public static void main(String[] args) {
            SpringApplication.run(EbsWebApplication.class, args);
        }

}
