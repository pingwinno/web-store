package com.study.service;

import com.study.TokenStorage;
import com.study.exception.AuthenticationException;
import com.study.exception.AuthorizationException;
import com.study.model.User;
import com.study.model.UserToken;
import com.study.model.enums.Role;
import com.study.persistance.user.UserRepository;
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
    private static final Map<Role, List<Pattern>> allowedPaths =
            Map.of(ADMIN, List.of(ROOT, SEARCH, EDIT, ADD, LOGIN, LOGOUT),
                    USER, List.of(ROOT, SEARCH, LOGIN, LOGOUT),
                    GUEST, List.of(ROOT, SEARCH, LOGIN));
    private final UserRepository userRepository;
    private final TokenStorage tokenStorage;

    public SecurityService(UserRepository userRepository, TokenStorage tokenStorage) {
        this.userRepository = userRepository;
        this.tokenStorage = tokenStorage;
    }

    public void validateToken(String token, String path) {
        var expiredTime = Instant.now().getEpochSecond() + DEFAULT_LIFETIME;
        var tokenEntity = tokenStorage.getTokenEntity(token);
        var role = GUEST;
        if (tokenEntity.isPresent() && tokenEntity.get().getTimeStamp() < expiredTime) {
            role = tokenEntity.get().getUser().getRole();
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
        var user = userRepository.findByName(userName)
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
                                   .timeStamp(Instant.now().getEpochSecond())
                                   .user(user)
                                   .build();
        tokenStorage.saveToken(tokenEntity);
        return tokenEntity;
    }
}
