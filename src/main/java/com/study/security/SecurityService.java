package com.study.security;

import com.study.exception.AuthenticationException;
import com.study.exception.AuthorizationException;
import com.study.model.enums.Role;
import com.study.security.model.User;
import com.study.security.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.study.model.enums.Role.*;

@Slf4j
public class SecurityService {
    private final static int DEFAULT_LIFETIME = 14400;
    private final static Pattern ROOT = Pattern.compile("/");
    private final static Pattern SEARCH = Pattern.compile("/search.*");
    private final static Pattern ADD = Pattern.compile("/add");
    private final static Pattern EDIT = Pattern.compile("/edit/.*");
    private final static Pattern LOGIN = Pattern.compile("/login");
    private final static Pattern LOGOUT = Pattern.compile("/logout");
    private final static Pattern BASKET = Pattern.compile("/basket.*");
    private static final Map<Role, List<Pattern>> allowedPaths =
            Map.of(ADMIN, List.of(ROOT, SEARCH, EDIT, ADD, LOGIN, LOGOUT),
                    USER, List.of(ROOT, SEARCH, LOGIN, LOGOUT, BASKET),
                    GUEST, List.of(ROOT, SEARCH, LOGIN));
    private final UserService userService;
    private final TokenStorage tokenStorage;

    public SecurityService(UserService userService, TokenStorage tokenStorage) {
        this.userService = userService;
        this.tokenStorage = tokenStorage;
    }

    public void validateToken(String token, String path) {
        var tokenEntity = tokenStorage.getTokenEntity(token);
        var role = GUEST;
        if (tokenEntity.isPresent() && tokenEntity.get().getExpirationTime() > Instant.now().getEpochSecond()) {
            role = tokenEntity.get().getUser().getRole();
            log.info("user {} is {} ", tokenEntity.get().getUser().getName(), role);
        }
        validatePath(role, path);
    }

    private void validatePath(Role role, String path) {
        log.info("Role {} path {}", role, path);
        allowedPaths.get(role)
                    .stream()
                    .filter(allowedPath -> allowedPath.matcher(path).matches())
                    .findFirst()
                    .orElseThrow(AuthorizationException::new);
    }

    public UserToken login(String userName, String password) {
        var user = userService.getByName(userName)
                              .orElseThrow(AuthenticationException::new);
        var encodedPassword = DigestUtils.sha256Hex(password + user.getSalt());
        if (Objects.equals(encodedPassword, user.getPassword())) {
            return generateCookie(user);
        }
        throw new AuthenticationException();
    }

    public void logout(String token) {
        tokenStorage.removeCookie(token);
    }

    private UserToken generateCookie(User user) {
        var token = UUID.randomUUID().toString();
        var tokenEntity = UserToken.builder()
                                   .token(token)
                                   .expirationTime(Instant.now().getEpochSecond() + DEFAULT_LIFETIME)
                                   .user(user)
                                   .build();
        tokenStorage.saveToken(tokenEntity);
        return tokenEntity;
    }
}