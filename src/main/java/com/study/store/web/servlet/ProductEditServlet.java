package com.study.store.web.servlet;

import com.study.di.ServiceLocator;
import com.study.store.model.Product;
import com.study.store.service.ProductService;
import com.study.store.web.template.TemplateProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ProductEditServlet extends HttpServlet {

    private final ProductService productService = ServiceLocator.getDependency(ProductService.class);
    private final TemplateProvider templateProvider = ServiceLocator.getDependency(TemplateProvider.class);


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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            var product = Product.builder()
                                 .id(id)
                                 .name(req.getParameter("productName"))
                                 .description("description")
                                 .price(Double.parseDouble(req.getParameter("price")))
                                 .build();
            productService.update(product);
            resp.sendRedirect("/");
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }

    private long getIdFromPath(String url) {
        String idStr = url.substring(url.lastIndexOf('/') + 1);
        return Long.parseLong(idStr);
    }
}
