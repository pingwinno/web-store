package com.study.store.web.servlet;

import com.study.ApplicationContext;
import com.study.store.web.listener.InitListener;
import com.study.store.service.ProductService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProductDeleteServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute(
                InitListener.APPLICATION_CONTEXT);
        productService = applicationContext.getBean(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var url = req.getRequestURI();
            var id = getIdFromPath(url);
            productService.delete(id);
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
