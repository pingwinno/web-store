package com.study.store.security;

import com.study.store.exception.AuthenticationException;
import com.study.store.model.enums.Role;
import com.study.store.security.model.Credentials;
import com.study.store.security.model.User;
import com.study.store.security.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class SecurityService {
    private static final UserToken GUEST_USER = UserToken.builder()
                                                         .user(User.builder()
                                                                   .role(Role.GUEST)
                                                                   .build())
                                                         .build();
    private final UserService userService;
    private final TokenStorage tokenStorage;
    @Value(value = "${security.token.lifetime}")
    private int defaultLifetime = 14400;

    public SecurityService(UserService userService, TokenStorage tokenStorage) {
        this.userService = userService;
        this.tokenStorage = tokenStorage;
    }

    public UserToken getToken(String token) {
        var tokenEntity = tokenStorage.getTokenEntity(token);

        if (tokenEntity.isPresent() && tokenEntity.get()
                                                  .getExpirationTime() > Instant.now()
                                                                                .getEpochSecond()) {
            return tokenEntity.get();
        }
        return GUEST_USER;
    }


    public UserToken login(Credentials credentials) {
        var user = userService.getByName(credentials.getUserName())
                              .orElseThrow(AuthenticationException::new);
        var encodedPassword = DigestUtils.sha256Hex(credentials.getPassword() + user.getSalt());
        if (Objects.equals(encodedPassword, user.getPassword())) {
            return generateToken(user);
        }
        throw new AuthenticationException();
    }

    public void logout(String token) {
        tokenStorage.removeToken(token);
    }

    private UserToken generateToken(User user) {
        var token = UUID.randomUUID()
                        .toString();
        var tokenEntity = UserToken.builder()
                                   .token(token)
                                   .expirationTime(Instant.now()
                                                          .getEpochSecond() + defaultLifetime)
                                   .user(user)
                                   .build();
        tokenStorage.saveToken(tokenEntity);
        return tokenEntity;
    }
}
