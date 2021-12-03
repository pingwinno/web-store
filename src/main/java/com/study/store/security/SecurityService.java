package com.study.store.security;

import com.study.store.exception.AuthenticationException;
import com.study.store.model.enums.Role;
import com.study.store.security.model.User;
import com.study.store.security.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class SecurityService {
    private final static int DEFAULT_LIFETIME = 14400;
    private static final UserToken GUEST_USER = UserToken.builder()
                                                         .user(User.builder()
                                                                   .role(Role.GUEST)
                                                                   .build())
                                                         .build();
    private UserService userService;
    private TokenStorage tokenStorage;

    public UserToken getToken(String token) {
        var tokenEntity = tokenStorage.getTokenEntity(token);

        if (tokenEntity.isPresent() && tokenEntity.get()
                                                  .getExpirationTime() > Instant.now()
                                                                                .getEpochSecond()) {
            return tokenEntity.get();
        }
        return GUEST_USER;
    }


    public UserToken login(String userName, String password) {
        var user = userService.getByName(userName)
                              .orElseThrow(AuthenticationException::new);
        var encodedPassword = DigestUtils.sha256Hex(password + user.getSalt());
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
                                                          .getEpochSecond() + DEFAULT_LIFETIME)
                                   .user(user)
                                   .build();
        tokenStorage.saveToken(tokenEntity);
        return tokenEntity;
    }
}
