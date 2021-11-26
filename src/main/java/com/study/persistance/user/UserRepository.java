package com.study.persistance.user;

import com.study.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByName(String name);

    void save(User user);

    void updatePassword(User user);

    void delete(long id);
}
