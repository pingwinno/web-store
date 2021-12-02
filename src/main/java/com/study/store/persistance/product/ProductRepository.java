package com.study.store.persistance.product;

import com.study.store.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    List<Product> findAllByIds(List<Long> ids);

    Optional<Product> findById(long id);

    List<Product> search(String query);

    void save(Product product);

    void update(Product product);

    void delete(long id);
}
