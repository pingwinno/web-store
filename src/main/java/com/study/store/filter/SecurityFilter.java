package com.study.store.filter;

import com.study.ApplicationContext;
import com.study.store.exception.AuthenticationException;
import com.study.store.exception.AuthorizationException;
import com.study.store.listener.InitListener;
import com.study.store.security.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class SecurityFilter implements Filter {

    private static final String LOGIN_PATH = "/login";
    private final static String COOKIE_NAME = "user-token";
    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) {
        ApplicationContext applicationContext = (ApplicationContext) filterConfig.getServletContext()
                                                                                 .getAttribute(
                                                                                         InitListener.APPLICATION_CONTEXT);
        securityService = applicationContext.getBean(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var path = httpRequest.getServletPath();

        var cookies = httpRequest.getCookies();

        var token = cookies != null
                ? Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName()
                                                .equals(COOKIE_NAME))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null)
                : null;
        try {
            securityService.validateToken(token, path);
            chain.doFilter(httpRequest, httpResponse);
        } catch (AuthorizationException e) {
            httpResponse.sendError(e.getResponseCode());
        } catch (AuthenticationException e) {
            httpResponse.sendRedirect(LOGIN_PATH);
        }
    }

    @Override
    public void destroy() {

    }
}
