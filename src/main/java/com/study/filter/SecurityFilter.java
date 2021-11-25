package com.study.filter;

import com.study.exception.AuthenticationException;
import com.study.exception.AuthorizationException;
import com.study.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class SecurityFilter implements Filter {

    private static final String LOGIN_PATH = "/login";

    private final SecurityService service;

    public SecurityFilter(SecurityService service) {
        this.service = service;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var path = httpRequest.getServletPath();
        if (Objects.equals(path, LOGIN_PATH)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        var cookies = httpRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            httpResponse.sendRedirect(LOGIN_PATH);
            return;
        }
        var cookie = cookies[0];
        try {
            service.validateToken(cookie.getValue(), path);
        } catch (AuthorizationException e) {
            httpResponse.sendError(e.getResponseCode());
            return;
        } catch (AuthenticationException e) {
            httpResponse.sendRedirect(LOGIN_PATH);
            return;
        }
        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
