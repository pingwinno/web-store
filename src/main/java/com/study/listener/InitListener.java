package com.study.listener;

import com.study.ConfigProvider;
import com.study.security.TokenStorage;
import com.study.persistance.product.impl.JdbcProductRepository;
import com.study.persistance.user.impl.JdbcUserRepository;
import com.study.service.ProductService;
import com.study.security.SecurityService;
import com.study.web.template.TemplateProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static com.study.model.enums.ContextInstance.*;

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
        var securityService = new SecurityService(userRepository, tokenStorage);
        servletContextEvent.getServletContext().setAttribute(
                PRODUCT_SERVICE.getName(),
                productService);
        servletContextEvent.getServletContext().setAttribute(
                TEMPLATE_PROVIDER.getName(),
                templateProvider);
        servletContextEvent.getServletContext().setAttribute(
                SECURITY_SERVICE.getName(),
                securityService);
        servletContextEvent.getServletContext().setAttribute(
                TOKEN_STORAGE.getName(),
                tokenStorage);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
