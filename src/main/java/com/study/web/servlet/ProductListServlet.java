package com.study.web.servlet;

import com.study.service.ProductService;
import com.study.web.template.TemplateProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ProductListServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateProvider templateProvider;

    public ProductListServlet(ProductService productService, TemplateProvider templateProvider) {
        this.productService = productService;
        this.templateProvider = templateProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var params = Map.of("products", productService.getAll());
            var data = templateProvider.writePage(params, "list.ftl");
            resp.getOutputStream()
                .write(data);
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException(e);
        }
    }
}
