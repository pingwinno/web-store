package com.study.store.listener;

import com.study.store.ConfigProvider;
import com.study.store.model.enums.ContextInstance;
import com.study.store.persistance.product.impl.JdbcProductRepository;
import com.study.store.persistance.user.impl.JdbcUserRepository;
import com.study.store.security.SecurityService;
import com.study.store.service.BasketService;
import com.study.store.service.ProductService;
import com.study.store.security.TokenStorage;
import com.study.store.security.UserService;
import com.study.store.web.template.TemplateProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.error("initialize context");
        ConfigProvider configProvider = new ConfigProvider();
        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(configProvider.getDriverClassName());
        hikariConfig.setUsername(configProvider.getDbUser());
        hikariConfig.setPassword(configProvider.getDbPassword());
        hikariConfig.setJdbcUrl(configProvider.getDbUrl());
        var dataSource = new HikariDataSource(hikariConfig);
        var productRepository = new JdbcProductRepository(dataSource);
        var userRepository = new JdbcUserRepository(dataSource);
        var productService = new ProductService(productRepository);
        var templateProvider = new TemplateProvider();
        var tokenStorage = new TokenStorage();
        var userService = new UserService(userRepository);
        var securityService = new SecurityService(userService, tokenStorage);
        var basketService = new BasketService(productService);
        servletContextEvent.getServletContext().setAttribute(
                ContextInstance.PRODUCT_SERVICE.getName(),
                productService);
        servletContextEvent.getServletContext().setAttribute(
                ContextInstance.TEMPLATE_PROVIDER.getName(),
                templateProvider);
        servletContextEvent.getServletContext().setAttribute(
                ContextInstance.SECURITY_SERVICE.getName(),
                securityService);
        servletContextEvent.getServletContext().setAttribute(
                ContextInstance.TOKEN_STORAGE.getName(),
                tokenStorage);
        servletContextEvent.getServletContext().setAttribute(
                ContextInstance.BASKET_SERVICE.getName(),
                basketService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
