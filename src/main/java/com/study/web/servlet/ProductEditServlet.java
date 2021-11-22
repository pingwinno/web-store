package com.study.web.servlet;

import com.study.model.Product;
import com.study.service.ProductService;
import com.study.web.template.TemplateProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class ProductEditServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateProvider templateProvider;

    public ProductEditServlet(ProductService productService, TemplateProvider templateProvider) {
        this.productService = productService;
        this.templateProvider = templateProvider;
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            var params = Map.of("product", productService.getById(id));
            var data = templateProvider.writePage(params, "edit.ftl");
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            var product = Product.builder()
                    .id(id)
                    .name(req.getParameter("productName"))
                    .price(Double.parseDouble(req.getParameter("price")))
                    .build();
            productService.update(product);
            resp.sendRedirect("/");
        } catch (Throwable e) {
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException(e);
        }
    }

    private long getIdFromPath(String url) {
        String idStr = url.substring(url.lastIndexOf('/') + 1);
        return Long.parseLong(idStr);
    }
}
