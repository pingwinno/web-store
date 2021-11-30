package com.study.store.web.servlet;

import com.study.di.ServiceLocator;
import com.study.store.service.ProductService;
import com.study.store.web.template.TemplateProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ProductSearchServlet extends HttpServlet {
    private final static String SEARCH_TEMPLATE = "%s-search.ftl";
    private final ProductService productService = ServiceLocator.getDependency(ProductService.class);
    private final TemplateProvider templateProvider = ServiceLocator.getDependency(TemplateProvider.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var resultMap = Map.of("products",
                    productService.search(req.getParameter("searchInput")));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var templateName = String.format(SEARCH_TEMPLATE, req.getAttribute("prefix"));
            var data = templateProvider.writePage(resultMap, templateName);
            resp.getOutputStream()
                .write(data);
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }
}