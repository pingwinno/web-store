package com.study.presentation;

import com.study.service.ProductService;
import com.study.service.TemplateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductListServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateService templateService;

    public ProductListServlet(ProductService productService, TemplateService templateService) {
        this.productService = productService;
        this.templateService = templateService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            templateService.writeListPage(productService.getAllProducts(), resp.getWriter());
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
        }
    }
}
