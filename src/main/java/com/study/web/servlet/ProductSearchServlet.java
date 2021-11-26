package com.study.web.servlet;

import com.study.service.ProductService;
import com.study.web.template.TemplateProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.study.model.enums.ContextInstance.PRODUCT_SERVICE;
import static com.study.model.enums.ContextInstance.TEMPLATE_PROVIDER;

@Slf4j
public class ProductSearchServlet extends HttpServlet {
    private final static String SEARCH_TEMPLATE = "%s-search.ftl";
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
            var resultMap = Map.of("products",
                    productService.search(req.getParameter("searchInput")));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var templateName = String.format(SEARCH_TEMPLATE, req.getAttribute("prefix"));
            var data = templateProvider.writePage(resultMap, templateName);
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
