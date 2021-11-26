package com.study.web.servlet;

import com.study.model.Product;
import com.study.service.ProductService;
import com.study.web.template.TemplateProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.study.model.enums.ContextInstance.PRODUCT_SERVICE;
import static com.study.model.enums.ContextInstance.TEMPLATE_PROVIDER;

@Slf4j
public class ProductEditServlet extends HttpServlet {

    private ProductService productService;
    private TemplateProvider templateProvider;

    @Override
    public void init() {
        productService = (ProductService) getServletContext().getAttribute(
                PRODUCT_SERVICE.getName());
        templateProvider = (TemplateProvider) getServletContext().getAttribute(
                TEMPLATE_PROVIDER.getName());
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
