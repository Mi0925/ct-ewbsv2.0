package cn.comtom.linkage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        RestTemplate template = new RestTemplate(factory);
        template.getMessageConverters().add(0,converter);
        return template;
    }

    @Bean
    public ClientHttpRequestFactory factory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // ms
        factory.setReadTimeout(30000);
        // ms
        factory.setConnectTimeout(3000);
        return factory;
    }
}
