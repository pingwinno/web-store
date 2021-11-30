package com.study.store.security;

import com.study.store.persistance.user.UserRepository;
import com.study.store.security.model.User;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


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
