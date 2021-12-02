package com.study.store.web.servlet;

import com.study.ApplicationContext;
import com.study.store.listener.InitListener;
import com.study.store.model.Product;
import com.study.store.service.ProductService;
import com.study.store.web.template.TemplateProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProductAddServlet extends HttpServlet {
    private ProductService productService;
    private TemplateProvider templateProvider;


    @Override
    public void init() {
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute(
                InitListener.APPLICATION_CONTEXT);
        productService = applicationContext.getBean(ProductService.class);
        templateProvider = applicationContext.getBean(TemplateProvider.class);
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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }
}
