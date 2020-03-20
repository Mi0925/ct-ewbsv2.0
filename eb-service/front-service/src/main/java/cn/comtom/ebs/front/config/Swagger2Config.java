package cn.comtom.ebs.front.config;

import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.OpenRest;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author guomao
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    private String basePackage = "cn.comtom.ebs.front.main";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("open接口").select()
                .apis(Predicates.and(RequestHandlerSelectors.basePackage(basePackage),
                        RequestHandlerSelectors.withClassAnnotation(OpenRest.class),
                        Predicates.not(RequestHandlerSelectors.withMethodAnnotation(AuthRest.class))))
                .build().apiInfo(apiInfo());
    }

    @Bean
    public Docket createProtectRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).order(0);
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName("auth接口").select()
                .apis(Predicates.and(RequestHandlerSelectors.basePackage(basePackage),
                        Predicates.or(RequestHandlerSelectors.withClassAnnotation(AuthRest.class),
                                RequestHandlerSelectors.withMethodAnnotation(AuthRest.class))))
                .build().globalOperationParameters(pars)
                .apiInfo(safeApiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("应急广播系统   OPEN RESTful API文档").description("开放接口参考和查看API详细信息")
                .termsOfServiceUrl("http://www.comtom.cn/")
                .contact(new Contact("wujiang", "", ""))
                .version("1.0").build();
    }


    private ApiInfo safeApiInfo() {
        return new ApiInfoBuilder().title("应急广播系统   SAFE RESTful API文档").description("安全接口参考和查看API详细信息")
                .termsOfServiceUrl("http://www.comtom.cn/")
                .contact(new Contact("wujiang", "", ""))
                .version("1.0").build();
    }


}
