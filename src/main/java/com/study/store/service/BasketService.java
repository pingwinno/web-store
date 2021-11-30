package com.study.store.service;

import com.study.ioc.DependencyContainer;
import com.study.store.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasketService {
    private final ProductService productService = DependencyContainer.getDependency(ProductService.class);

    public List<Product> getBasketProducts(List<Long> ids) {
        var products = productService.getAll();
        var basket = new ArrayList<Product>();
        for (Product product : products) {
            for (Long id : ids) {
                if (Objects.equals(product.getId(), id)) {
                    basket.add(product);
                }
            }
        }
        return basket;
    }
}
