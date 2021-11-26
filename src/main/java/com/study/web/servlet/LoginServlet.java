package com.study.web.servlet;

import com.study.exception.HttpException;
import com.study.security.SecurityService;
import com.study.web.template.TemplateProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.study.model.enums.ContextInstance.SECURITY_SERVICE;
import static com.study.model.enums.ContextInstance.TEMPLATE_PROVIDER;

@Slf4j
public class LoginServlet extends HttpServlet {

    private final static String COOKIE_NAME = "user-token";
    private SecurityService securityService;
    private TemplateProvider templateProvider;

    @Override
    public void init() {
        securityService = (SecurityService) getServletContext().getAttribute(
                SECURITY_SERVICE.getName());
        templateProvider = (TemplateProvider) getServletContext().getAttribute(
                TEMPLATE_PROVIDER.getName());
    }

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
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException("Fail to send response", e);
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
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            log.error("Fail to send response", e);
            throw new RuntimeException("Fail to send response", e);
        }
    }
}
