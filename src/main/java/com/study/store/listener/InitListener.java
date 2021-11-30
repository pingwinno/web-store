package com.study.store.listener;

import com.study.ioc.DependencyContainer;
import com.study.store.ConfigProvider;
import com.study.store.persistance.product.ProductRepository;
import com.study.store.persistance.product.impl.JdbcProductRepository;
import com.study.store.security.SecurityService;
import com.study.store.security.TokenStorage;
import com.study.store.security.UserService;
import com.study.store.security.persistence.user.UserRepository;
import com.study.store.security.persistence.user.impl.JdbcUserRepository;
import com.study.store.service.BasketService;
import com.study.store.service.ProductService;
import com.study.store.web.template.TemplateProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

@Slf4j
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ConfigProvider configProvider = new ConfigProvider();
        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(configProvider.getDriverClassName());
        hikariConfig.setUsername(configProvider.getDbUser());
        hikariConfig.setPassword(configProvider.getDbPassword());
        hikariConfig.setJdbcUrl(configProvider.getDbUrl());
        DependencyContainer.addDependency(DataSource.class, new HikariDataSource(hikariConfig));
        DependencyContainer.addDependency(ProductRepository.class, new JdbcProductRepository());
        DependencyContainer.addDependency(UserRepository.class, new JdbcUserRepository());
        DependencyContainer.addDependency(ProductService.class, new ProductService());
        DependencyContainer.addDependency(TemplateProvider.class, new TemplateProvider());
        DependencyContainer.addDependency(TokenStorage.class, new TokenStorage());
        DependencyContainer.addDependency(UserService.class, new UserService());
        DependencyContainer.addDependency(SecurityService.class, new SecurityService());
        DependencyContainer.addDependency(BasketService.class, new BasketService());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
