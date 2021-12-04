package com.study.store.web.controller;

import com.study.store.security.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogoutController {
    private final static String COOKIE_NAME = "user-token";

    private final SecurityService securityService;

    public LogoutController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/logout")
    protected String logout(HttpServletRequest request, HttpServletResponse response) {
        var cookie = WebUtils.getCookie(request, COOKIE_NAME);
        securityService.logout(cookie.getValue());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
