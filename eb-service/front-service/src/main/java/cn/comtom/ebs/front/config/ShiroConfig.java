package cn.comtom.ebs.front.config;

import cn.comtom.ebs.front.main.oauth2.OAuth2Filter;
import cn.comtom.ebs.front.main.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Shiro配置
 *
 * @author comtom
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //sessionManager.setSessionIdCookieEnabled(false);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
   //     filters.put("localFilter", new LocaleFilter());
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/safeRest/**", "oauth2");
        filterMap.put("/openRest/oauth2/logout", "oauth2");
        filterMap.put("/openRest/oauth2/checkPermission", "oauth2");
        filterMap.put("/openRest/**", "anon");

        filterMap.put("/", "anon");
        filterMap.put("/**/*.css", "anon");
        filterMap.put("/**/*.js", "anon");
        filterMap.put("/**/*.html", "anon");
        filterMap.put("/**/*.htm", "anon");
        filterMap.put("/**/*.png", "anon");
        filterMap.put("/**/*.ico", "anon");
        filterMap.put("/**/captcha.jpg", "anon");
        filterMap.put("/**/*.jpg", "anon");
        filterMap.put("/**/*.mp3", "anon");
        filterMap.put("/**/*.gif", "anon");
        filterMap.put("/fonts/**", "anon");
        filterMap.put("/android/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/i18n/**", "anon");

        filterMap.put("/**/*.map", "anon");
        filterMap.put("/**/*.eot", "anon");
        filterMap.put("/**/*.ttf", "anon");
        filterMap.put("/**/*.woff", "anon");
        filterMap.put("/**/*.woff2", "anon");

        //swagger配置
        filterMap.put("/swagger**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-resources/configuration/ui", "anon");


        filterMap.put("/**", "oauth2");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
