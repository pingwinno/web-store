package com.study;

import com.study.persistance.factory.EntityManagerStorage;
import com.study.persistance.impl.ProductRepositoryImpl;
import com.study.presentation.ProductAddServlet;
import com.study.presentation.ProductDeleteServlet;
import com.study.presentation.ProductEditServlet;
import com.study.presentation.ProductListServlet;
import com.study.service.ProductService;
import com.study.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

@Slf4j
public class Starter {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        try (var entityManagerStorage = new EntityManagerStorage("store-persistence")) {
            var productRepository = new ProductRepositoryImpl(entityManagerStorage);
            var productService = new ProductService(productRepository);
            var templateService = new TemplateService();

            var productListServlet = new ProductListServlet(productService, templateService);
            var productAddServlet = new ProductAddServlet(productService);
            var productEditServlet = new ProductEditServlet(productService, templateService);
            var productDeleteServlet = new ProductDeleteServlet(productService);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(new ServletHolder(productDeleteServlet), "/delete/*");
            context.addServlet(new ServletHolder(productEditServlet), "/edit/*");
            context.addServlet(new ServletHolder(productAddServlet), "/add");
            context.addServlet(new ServletHolder(productListServlet), "/*");
            var portString = System.getenv("PORT");
            try {
                port = Integer.parseInt(portString);
            }catch (NumberFormatException e){
                log.warn("Can't set port from env. Default port is 8080");
            }
            Server server = new Server(port);
            server.setHandler(context);
            server.start();
            server.join();
        }
    }
}
