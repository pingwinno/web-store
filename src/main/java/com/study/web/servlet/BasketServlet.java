package com.study.web.servlet;

import com.study.exception.HttpException;
import com.study.service.BasketService;
import com.study.web.template.TemplateProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.study.model.enums.ContextInstance.BASKET_SERVICE;
import static com.study.model.enums.ContextInstance.TEMPLATE_PROVIDER;

@Slf4j
public class BasketServlet extends HttpServlet {

    private static final String BASKET = "basket";
    private TemplateProvider templateProvider;
    private BasketService basketService;

    @Override
    public void init() {
        templateProvider = (TemplateProvider) getServletContext().getAttribute(
                TEMPLATE_PROVIDER.getName());
        basketService = (BasketService) getServletContext().getAttribute(
                BASKET_SERVICE.getName());
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var basket = (List<Long>) req.getSession().getAttribute(BASKET);
            var params = basket != null
                    ? Map.of("products", basketService.getBasketProducts(basket))
                    : Map.of("products",List.of());
            var data = templateProvider.writePage(params, "basket.ftl");
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
            var session = req.getSession();
            var url = req.getRequestURI();
            if (session.getAttribute(BASKET) == null) {
                session.setAttribute(BASKET, new ArrayList<Long>());
            }
            var productId = Long.valueOf(req.getParameter("productId"));
            var basket = (List<Long>) session.getAttribute(BASKET);
            if (url.contains("add")) {
                basket.add(productId);
                resp.sendRedirect("/");
            } else if (url.contains("delete")) {
                basket.remove(productId);
                resp.sendRedirect("/basket");
            }
        } catch (HttpException e) {
            resp.setStatus(e.getResponseCode());
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }

}
