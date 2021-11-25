package com.study.service;

import com.study.TokenStorage;
import com.study.exception.AuthenticationException;
import com.study.model.User;
import com.study.model.UserToken;
import com.study.persistance.user.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class SecurityService {
    private final static int DEFAULT_LIFETIME = 14400;
    private final static String COOKIE_NAME = "user-token";
    private final UserRepository userRepository;
    private final TokenStorage tokenStorage;

    public SecurityService(UserRepository userRepository, TokenStorage tokenStorage) {
        this.userRepository = userRepository;
        this.tokenStorage = tokenStorage;
    }

    public void validateToken(String token, String path) {
        var expiredTime = Instant.now().getEpochSecond() + DEFAULT_LIFETIME;
        var tokenEntity = tokenStorage.getTokenEntity(token);
        if (tokenEntity.isPresent() && tokenEntity.get().getTimeStamp() < expiredTime) {
            return;
        }
        throw new AuthenticationException();
    }

    @SneakyThrows
    public Cookie login(String userName, String password) {
        var user = userRepository.findByName(userName)
                                 .orElseThrow(AuthenticationException::new);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        var encodedPassword = DigestUtils.sha256Hex(password + user.getSalt());
        if (Objects.equals(encodedPassword, user.getPassword())) {
            return generateCookie(user);
        }
        throw new AuthenticationException();
    }

    @SneakyThrows
    public void logout(String token) {
        tokenStorage.removeCookie(token);
    }

    private Cookie generateCookie(User user) {
        var token = UUID.randomUUID().toString();
        var tokenEntity = UserToken.builder()
                                   .token(token)
                                   .timeStamp(Instant.now().getEpochSecond())
                                   .user(user)
                                   .build();
        tokenStorage.saveToken(tokenEntity);
        var cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(DEFAULT_LIFETIME);
        return cookie;
    }
}