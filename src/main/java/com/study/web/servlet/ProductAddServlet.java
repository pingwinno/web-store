package com.study.web.servlet;

import com.study.model.Product;
import com.study.service.ProductService;
import com.study.web.template.TemplateProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.study.model.enums.ContextInstance.PRODUCT_SERVICE;
import static com.study.model.enums.ContextInstance.TEMPLATE_PROVIDER;

@Slf4j
public class ProductAddServlet extends HttpServlet {
    private ProductService productService;
    private TemplateProvider templateProvider;

    @Override
    public void init() {
        productService = (ProductService) getServletContext().getAttribute(
                PRODUCT_SERVICE.getName());
        templateProvider = (TemplateProvider) getServletContext().getAttribute(
                TEMPLATE_PROVIDER.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var data = templateProvider.writePage("add.ftl");
            resp.getOutputStream()
                .write(data);
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var product = Product.builder()
                                 .name(req.getParameter("productName"))
                                 .description("description")
                                 .price(Double.parseDouble(req.getParameter("price")))
                                 .build();
            productService.create(product);
            resp.sendRedirect("/");
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException(e);
        }
    }
}
