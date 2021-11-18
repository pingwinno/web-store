package com.study;

import com.study.persistance.factory.EntityManagerStorage;
import com.study.persistance.impl.ProductRepositoryImpl;
import com.study.presentation.ProductAddServlet;
import com.study.presentation.ProductDeleteServlet;
import com.study.presentation.ProductEditServlet;
import com.study.presentation.ProductListServlet;
import com.study.service.ProductService;
import com.study.service.TemplateService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Starter {
    public static void main(String[] args) throws Exception {
        try (var entityManagerStorage = new EntityManagerStorage("store-persistence");) {
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

            Server server = new Server(8080);
            server.setHandler(context);
            server.start();
        }
    }
}
