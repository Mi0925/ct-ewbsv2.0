package cn.comtom.ebs.front.main.oauth2;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * oauth2过滤器
 */
@Slf4j
public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }

   /* public static final String LANGUAGE_KEY = "cbsLang";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    *//**
     * 优先从request中取地区，没有就从session中取地区，还没有再从cookie中取地区
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     *//*
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = IPUtils.getIpAddr((HttpServletRequest) request);
        log.debug(ip);
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        Locale locale = null;

        String languageParameter = request.getParameter(LANGUAGE_KEY);
        if (StringUtils.isNotBlank(languageParameter)) {
            locale = getLocale(languageParameter);
        } else {
            Locale sessionLocale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
            if (sessionLocale == null) {
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(LANGUAGE_KEY)) {
                            locale = getLocale(cookie.getValue());
                            break;
                        }
                    }
                }
            }

        }
        if (locale != null) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        }
        chain.doFilter(request, response);
    }

    private Locale getLocale(String localeString) {
        Locale locale = Locale.CHINA;
        switch (localeString) {
            case "zh_CN":
                locale = Locale.CHINA;
                break;
            case "en_US":
                locale = Locale.US;
                break;
            default:
        }
        return locale;
    }

    @Override
    public void destroy() {
    }*/
}
