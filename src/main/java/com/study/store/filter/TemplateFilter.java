package com.study.store.filter;


import com.study.ApplicationContext;
import com.study.store.listener.InitListener;
import com.study.store.model.enums.Role;
import com.study.store.security.TokenStorage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class TemplateFilter implements Filter {

    private final static String COOKIE_NAME = "user-token";
    private TokenStorage tokenStorage;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext applicationContext = (ApplicationContext) filterConfig.getServletContext()
                                                                                 .getAttribute(
                                                                                         InitListener.APPLICATION_CONTEXT);
        tokenStorage = applicationContext.getBean(TokenStorage.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;

        var cookies = httpRequest.getCookies();

        var token = cookies != null
                ? Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName()
                                                .equals(COOKIE_NAME))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null)
                : null;
        var templatePrefix = tokenStorage.getTokenEntity(token)
                                         .map(userToken -> userToken.getUser()
                                                                    .getRole())
                                         .orElse(Role.GUEST)
                                         .getName();
        servletRequest.setAttribute("prefix", templatePrefix);
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
