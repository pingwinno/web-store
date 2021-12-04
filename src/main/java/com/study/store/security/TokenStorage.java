package com.study.store.security;

import com.study.store.security.model.UserToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
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

    public void removeToken(String token) {
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
                                        .filter(userToken -> userToken.getExpirationTime() < Instant.now()
                                                                                                    .getEpochSecond())
                                        .map(UserToken::getToken)
                                        .collect(Collectors.toList());
            for (String expiredToken : expiredTokens) {
                tokenMap.remove(expiredToken);
            }
            log.debug("Token storage cleaned");
            Thread.sleep(CYCLE_DELAY);
        }
    }
}
