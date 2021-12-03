package com.study.store;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private final List<Long> products = new ArrayList<>();

    public void addProductId(Long id) {
        products.add(id);
    }

    public void removeProduct(Long id) {
        products.remove(id);
    }

    public List<Long> getProducts() {
        return new ArrayList<>(products);
    }
}
