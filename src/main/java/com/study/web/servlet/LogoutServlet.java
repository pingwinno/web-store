package com.study.web.servlet;

import com.study.security.SecurityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.study.model.enums.ContextInstance.SECURITY_SERVICE;

@Slf4j
public class LogoutServlet extends HttpServlet {

    private SecurityService securityService;

    @Override
    public void init() {
        securityService = (SecurityService) getServletContext().getAttribute(
                SECURITY_SERVICE.getName());
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var cookie = req.getCookies()[0];
            securityService.logout(cookie.getValue());
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            resp.sendRedirect("/");
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Fail to send response", e);
        }
    }
}
