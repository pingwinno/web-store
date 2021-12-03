package com.study.store.web.servlet;

import com.study.ApplicationContext;
import com.study.store.web.listener.InitListener;
import com.study.store.security.SecurityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogoutServlet extends HttpServlet {

    private SecurityService securityService;

    @Override
    public void init() {
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute(
                InitListener.APPLICATION_CONTEXT);
        securityService = applicationContext.getBean(SecurityService.class);
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
