package com.study.presentation;

import com.study.model.Product;
import com.study.service.ProductService;
import com.study.service.TemplateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

public class ProductEditServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateService templateService;

    public ProductEditServlet(ProductService productService, TemplateService templateService) {
        this.productService = productService;
        this.templateService = templateService;
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            templateService.writeEditPage(productService.getProductById(id), resp.getWriter());
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            var product = Product.builder()
                                 .id(id)
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

    private long getIdFromPath(String url) {
        String idStr = url.substring(url.lastIndexOf('/') + 1);
        return Long.parseLong(idStr);
    }
}
