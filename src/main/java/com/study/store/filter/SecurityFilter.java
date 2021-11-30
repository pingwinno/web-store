package com.study.store.filter;

import com.study.store.exception.AuthenticationException;
import com.study.store.exception.AuthorizationException;
import com.study.store.model.enums.ContextInstance;
import com.study.store.security.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class SecurityFilter implements Filter {

    private static final String LOGIN_PATH = "/login";
    private final static String COOKIE_NAME = "user-token";
    private SecurityService service;

    @Override
    public void init(FilterConfig filterConfig) {
        service = (SecurityService) filterConfig.getServletContext().getAttribute(
                ContextInstance.SECURITY_SERVICE.getName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var path = httpRequest.getServletPath();

        var cookies = httpRequest.getCookies();

        var token = cookies != null
                ? Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null)
                : null;
        try {
            service.validateToken(token, path);
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
