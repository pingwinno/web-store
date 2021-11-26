package com.study.web.servlet;

import com.study.service.ProductService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.study.model.enums.ContextInstance.PRODUCT_SERVICE;

@Slf4j
public class ProductDeleteServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = (ProductService) getServletContext().getAttribute(
                PRODUCT_SERVICE.getName());
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
