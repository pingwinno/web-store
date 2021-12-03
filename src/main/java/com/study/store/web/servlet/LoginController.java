package com.study.store.web.servlet;

import com.study.store.security.SecurityService;
import com.study.store.security.model.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    private final static String COOKIE_NAME = "user-token";
    private static final String LOGIN_TEMPLATE = "login";
    @Autowired
    private SecurityService securityService;

    @PostMapping
    protected String login(@ModelAttribute Credentials credentials, HttpServletResponse response) {
        var userToken = securityService.login(credentials);
        response.addCookie(new Cookie(COOKIE_NAME, userToken.getToken()));
        return "redirect:/";
    }

    @GetMapping
    protected String getLoginPage() {
        return LOGIN_TEMPLATE;
    }
}
