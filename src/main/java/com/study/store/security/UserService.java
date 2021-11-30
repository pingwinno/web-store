package com.study.store.security;

import com.study.di.ServiceLocator;
import com.study.store.security.model.User;
import com.study.store.security.persistence.user.UserRepository;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository = ServiceLocator.getDependency(UserRepository.class);

    public Optional<User> getByName(String user) {
        return userRepository.findByName(user);
    }


    public void create(User user) {
        userRepository.save(user);
    }

    public void updatePassword(User user) {
        userRepository.updatePassword(user);
    }

    public void delete(String name) {
        userRepository.delete(name);
    }
}
