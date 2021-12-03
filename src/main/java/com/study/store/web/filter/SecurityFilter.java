package com.study.store.web.filter;

import com.study.ApplicationContext;
import com.study.store.exception.AuthenticationException;
import com.study.store.exception.AuthorizationException;
import com.study.store.model.enums.Role;
import com.study.store.security.SecurityService;
import com.study.store.web.listener.InitListener;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class SecurityFilter implements Filter {
    private final static Pattern ROOT = Pattern.compile("/");
    private final static Pattern SEARCH = Pattern.compile("/search.*");
    private final static Pattern ADD = Pattern.compile("/add");
    private final static Pattern EDIT = Pattern.compile("/edit.*");
    private final static Pattern LOGIN = Pattern.compile("/login");
    private final static Pattern LOGOUT = Pattern.compile("/logout");
    private final static Pattern BASKET = Pattern.compile("/basket.*");
    private static final Map<Role, List<Pattern>> allowedPaths =
            Map.of(Role.ADMIN, List.of(ROOT, SEARCH, EDIT, ADD, LOGIN, LOGOUT),
                    Role.USER, List.of(ROOT, SEARCH, LOGIN, LOGOUT, BASKET),
                    Role.GUEST, List.of(ROOT, SEARCH, LOGIN));
    private static final String LOGIN_PATH = "/login";
    private final static String COOKIE_NAME = "user-token";
    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) {
        ApplicationContext applicationContext = (ApplicationContext) filterConfig.getServletContext()
                                                                                 .getAttribute(
                                                                                         InitListener.APPLICATION_CONTEXT);
        securityService = applicationContext.getBean(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var path = httpRequest.getServletPath();

        var cookies = httpRequest.getCookies();

        var token = cookies != null
                ? Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName()
                                                .equals(COOKIE_NAME))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null)
                : null;
        try {
            var userToken = securityService.getToken(token);
            validatePath(userToken.getUser()
                                  .getRole(), path);
            chain.doFilter(httpRequest, httpResponse);
        } catch (AuthorizationException e) {
            httpResponse.sendError(e.getResponseCode());
        } catch (AuthenticationException e) {
            httpResponse.sendRedirect(LOGIN_PATH);
        }
    }

    private void validatePath(Role role, String path) {
        allowedPaths.get(role)
                    .stream()
                    .filter(allowedPath -> allowedPath.matcher(path)
                                                      .matches())
                    .findFirst()
                    .orElseThrow(AuthorizationException::new);
    }

    @Override
    public void destroy() {

    }
}
