package com.study.filter;

import com.study.security.TokenStorage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

import static com.study.model.enums.ContextInstance.TOKEN_STORAGE;
import static com.study.model.enums.Role.GUEST;

@Slf4j
public class TemplateFilter implements Filter {

    private final static String COOKIE_NAME = "user-token";
    private TokenStorage tokenStorage;

    @Override
    public void init(FilterConfig filterConfig) {
        tokenStorage = (TokenStorage) filterConfig.getServletContext().getAttribute(
                TOKEN_STORAGE.getName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;

        var cookies = httpRequest.getCookies();

        var token = cookies != null
                ? Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null)
                : null;
        var templatePrefix = tokenStorage.getTokenEntity(token)
                                         .map(userToken -> userToken.getUser().getRole())
                                         .orElse(GUEST)
                                         .getName();
        servletRequest.setAttribute("prefix", templatePrefix);
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
