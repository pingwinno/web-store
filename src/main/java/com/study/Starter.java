package com.study;

import com.study.filter.SecurityFilter;
import com.study.persistance.product.impl.JpaProductRepository;
import com.study.persistance.user.impl.JpaUserRepository;
import com.study.service.ProductService;
import com.study.service.SecurityService;
import com.study.web.servlet.*;
import com.study.web.template.TemplateProvider;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import javax.persistence.Persistence;
import java.util.EnumSet;

import static com.study.ConfigProvider.PERSISTENCE;

@Slf4j
public class Starter {
    public static void main(String[] args) throws Exception {

        var configProvider = new ConfigProvider();
        configProvider.populateDriverFromEnv();
        var flyway = Flyway.configure()
                           .dataSource(configProvider.getDbUrl(),
                                   configProvider.getDbUser(),
                                   configProvider.getDbPassword())
                           .load();
        flyway.migrate();

        var entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE,
                configProvider.getProperties());
        try {
            var productRepository = new JpaProductRepository(entityManagerFactory);
            var userRepository = new JpaUserRepository(entityManagerFactory);
            var productService = new ProductService(productRepository);
            var templateProvider = new TemplateProvider();
            var tokenStorage = new TokenStorage();
            var securityService = new SecurityService(userRepository, tokenStorage);

            var loginServlet = new LoginServlet(securityService, templateProvider);
            var logoutServlet = new LogoutServlet(securityService);
            var productListServlet = new ProductListServlet(productService, templateProvider);
            var productAddServlet = new ProductAddServlet(productService, templateProvider);
            var productEditServlet = new ProductEditServlet(productService, templateProvider);
            var productSearchServlet = new ProductSearchServlet(productService, templateProvider);
            var productDeleteServlet = new ProductDeleteServlet(productService);

            var securityFilter = new SecurityFilter(securityService);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(new ServletHolder(loginServlet), "/login");
            context.addServlet(new ServletHolder(logoutServlet), "/logout");
            context.addServlet(new ServletHolder(productSearchServlet), "/search");
            context.addServlet(new ServletHolder(productDeleteServlet), "/delete/*");
            context.addServlet(new ServletHolder(productEditServlet), "/edit/*");
            context.addServlet(new ServletHolder(productAddServlet), "/add");
            context.addServlet(new ServletHolder(productListServlet), "/*");

            context.addFilter(new FilterHolder(securityFilter), "/*", EnumSet.allOf(DispatcherType.class));

            Server server = new Server(configProvider.getPort());
            server.setHandler(context);
            server.start();
            server.join();
        } finally {
            entityManagerFactory.close();
        }
    }
}
