package com.study.store.security.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class UserToken {
    private String token;
    private User user;
    private long expirationTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserToken userToken = (UserToken) o;
        return Objects.equals(token, userToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
