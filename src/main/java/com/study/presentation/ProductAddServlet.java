package com.study.presentation;

import com.study.model.Product;
import com.study.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductAddServlet extends HttpServlet {
    private final ProductService productService;

    public ProductAddServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getOutputStream()
                .write(this.getClass()
                           .getClassLoader()
                           .getResourceAsStream("templates/add.html")
                           .readAllBytes());
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var product = Product.builder()
                                 .name(req.getParameter("productName"))
                                 .price(
                                         Double.parseDouble(req.getParameter("price")))
                                 .build();
            productService.updateProduct(product);
            resp.sendRedirect("/");
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
        }
    }
}
