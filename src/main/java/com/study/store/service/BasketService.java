package com.study.store.service;

import com.study.store.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BasketService {
    private ProductService productService;

    public List<Product> getBasketProducts(List<Long> ids) {
        if (!ids.isEmpty()) {
            var products = productService.getAllByIds(ids);
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
        return Collections.emptyList();
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
