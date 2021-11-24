package com.study;

import com.study.model.UserToken;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStorage {

    private final Map<String, UserToken> tokenMap = new ConcurrentHashMap<>();

    public synchronized void saveToken(UserToken userToken) {
        tokenMap.put(userToken.getToken(), userToken);
    }

    public synchronized Optional<UserToken> getTokenEntity(String token) {
        return Optional.ofNullable(tokenMap.get(token));
    }

    public synchronized void removeCookie(String token) {
        tokenMap.remove(token);
    }
}
