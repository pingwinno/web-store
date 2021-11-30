package com.study.store.web.servlet;

import com.study.di.ServiceLocator;
import com.study.store.exception.HttpException;
import com.study.store.security.SecurityService;
import com.study.store.web.template.TemplateProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginServlet extends HttpServlet {

    private final static String COOKIE_NAME = "user-token";
    private final SecurityService securityService = ServiceLocator.getDependency(SecurityService.class);
    private final TemplateProvider templateProvider = ServiceLocator.getDependency(TemplateProvider.class);


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            var data = templateProvider.writePage("login.ftl");
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
            var userName = req.getParameter("userName");
            var password = req.getParameter("password");
            var userToken = securityService.login(userName, password);
            var cookie = new Cookie(COOKIE_NAME, userToken.getToken());
            resp.addCookie(cookie);
            resp.sendRedirect("/");
        } catch (HttpException e) {
            resp.setStatus(e.getResponseCode());
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }
}
