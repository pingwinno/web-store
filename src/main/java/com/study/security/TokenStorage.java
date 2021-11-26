package com.study.security;

import com.study.model.UserToken;
import lombok.SneakyThrows;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TokenStorage {

    private final static int CYCLE_DELAY = 60000;
    private final Map<String, UserToken> tokenMap = new ConcurrentHashMap<>();

    public TokenStorage() {
        startCleanupCycle();
    }

    public void saveToken(UserToken userToken) {
        tokenMap.put(userToken.getToken(), userToken);
    }

    public Optional<UserToken> getTokenEntity(String token) {
        return Optional.ofNullable(token != null ? tokenMap.get(token) : null);
    }

    public void removeCookie(String token) {
        tokenMap.remove(token);
    }

    private void startCleanupCycle() {
        new Thread(this::cleanUpTokens).start();
    }

    @SneakyThrows
    private void cleanUpTokens() {
        while (true) {
            var expiredTokens = tokenMap.values()
                                        .stream()
                                        .filter(userToken -> userToken.getExpirationTime() < Instant.now().getEpochSecond())
                                        .map(UserToken::getToken)
                                        .collect(Collectors.toList());
            for (String expiredToken : expiredTokens) {
                tokenMap.remove(expiredToken);
            }
            Thread.currentThread().wait(CYCLE_DELAY);
        }
    }
}
