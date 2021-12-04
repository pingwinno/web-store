package com.study.store.security;

import com.study.store.security.model.User;
import com.study.store.security.persistence.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
