package com.study.persistance.user;

import com.study.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByName(String name);

    User save(User product);

    User updatePassword(User product);

    void delete(long id);
}
