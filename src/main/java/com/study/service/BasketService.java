package com.study.service;

import com.study.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasketService {
    private final ProductService productService;

    public BasketService(ProductService productService) {
        this.productService = productService;
    }

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