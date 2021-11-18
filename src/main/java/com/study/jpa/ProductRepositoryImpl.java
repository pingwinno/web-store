package com.study.jpa;

import com.study.model.Product;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository{
    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void save(Product product) {

    }

    @Override
    public void delete(long id) {

    }
}
