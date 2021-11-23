package com.study.persistance.user;

import com.study.model.Product;
import com.study.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByName(String name);

    Product save(Product product);

    Product update(Product product);

    void delete(long id);
}
