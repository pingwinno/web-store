package com.study.web.servlet;

import com.study.exception.HttpException;
import com.study.service.SecurityService;
import com.study.web.template.TemplateProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginServlet extends HttpServlet {

    private final SecurityService securityService;
    private final TemplateProvider templateProvider;

    public LoginServlet(SecurityService productService, TemplateProvider templateProvider) {
        this.securityService = productService;
        this.templateProvider = templateProvider;
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
            resp.addCookie(securityService.login(userName, password));
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
