package cn.comtom.ebs.front.main.oauth2;


import cn.comtom.tools.response.ApiResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * oauth2过滤器
 */
@Slf4j
public class OAuth2Filter extends AuthenticatingFilter {
    private static Logger logger = LoggerFactory.getLogger(OAuth2Filter.class);

    private static String contentType = "application/json;charset=utf-8";

    private static String tokenKey = "token";


    /**
     * 获取请求的token
     *
     * @param httpRequest
     * @return
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader(tokenKey);

        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(tokenKey);
        }
        return token;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if (StringUtils.isEmpty(token) || "null".equals(token)) {
            return null;
        }

        return new OAuth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回token无效
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isEmpty(token) || "null".equals(token)) {
            writeJsonResponse(request, response, ErrorEnum.INVALID_TOKEN.getCode(), ErrorEnum.INVALID_TOKEN.getMsg());
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //处理登录失败的异常
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        if (e instanceof IncorrectCredentialsException) {
            writeJsonResponse(request, response, ErrorEnum.INVALID_TOKEN.getCode(), ErrorEnum.INVALID_TOKEN.getMsg());
        } else if (throwable instanceof UnknownAccountException) {
            logger.warn("登录失败:", e);
            writeJsonResponse(request, response, ErrorEnum.UNKNOWN_ACCOUNT.getCode(), ErrorEnum.UNKNOWN_ACCOUNT.getMsg());
        } else {
            writeJsonResponse(request, response, ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getMsg());

        }

        return false;
    }

    private void writeJsonResponse(ServletRequest request, ServletResponse response, String errorCode, String errorMsg) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType(contentType);
        ApiResponse ret;
        ret = ApiResponse.error(errorCode, errorMsg);

        //去除国际化
//        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
//        MessageComponent messageComponent = webApplicationContext.getBean(MessageComponent.class);
//        ret.setMsg(messageComponent.getMessage(ret.getCode()));

        String json = JSON.toJSONString(ret);
        try {
            httpResponse.getWriter().print(json);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

}
