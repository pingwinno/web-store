package com.study.persistance.product;

import com.study.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(long id);

    List<Product> search(String query);

    Product save(Product product);

    Product update(Product product);

    void delete(long id);
}
