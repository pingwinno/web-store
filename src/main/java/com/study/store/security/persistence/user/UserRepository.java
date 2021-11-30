package com.study.store.security.persistence.user;

import com.study.store.security.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByName(String name);

    void save(User user);

    void updatePassword(User user);

    void delete(String name);
}
