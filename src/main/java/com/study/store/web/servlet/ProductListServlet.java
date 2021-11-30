package com.study.store.web.servlet;

import com.study.ioc.DependencyContainer;
import com.study.store.service.ProductService;
import com.study.store.web.template.TemplateProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ProductListServlet extends HttpServlet {

    private final static String LIST_TEMPLATE = "%s-list.ftl";
    private final ProductService productService = DependencyContainer.getDependency(ProductService.class);
    private final TemplateProvider templateProvider = DependencyContainer.getDependency(TemplateProvider.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var params = Map.of("products", productService.getAll());
            var templateName = String.format(LIST_TEMPLATE, req.getAttribute("prefix"));
            var data = templateProvider.writePage(params, templateName);
            resp.getOutputStream()
                .write(data);
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }
}
