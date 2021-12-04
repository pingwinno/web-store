package com.study.store.web.filter;

import com.study.store.security.SecurityService;
import com.study.store.security.TokenStorage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Component
public class FilterDependencyInjector {

    private final ServletContext servletContext;
    private final TokenStorage tokenStorage;
    private final SecurityService securityService;

    public FilterDependencyInjector(ServletContext servletContext, TokenStorage tokenStorage, SecurityService securityService) {
        this.servletContext = servletContext;
        this.tokenStorage = tokenStorage;
        this.securityService = securityService;
    }

    @PostConstruct
    void injectDependenciesToContext() {
        servletContext.setAttribute("tokenStorage", tokenStorage);
        servletContext.setAttribute("securityService", securityService);
    }
}
