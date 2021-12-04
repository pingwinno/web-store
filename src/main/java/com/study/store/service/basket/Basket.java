package com.study.store.service.basket;

import com.study.store.service.ProductService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Basket {
    private final ProductService productService;
    private final Map<Long, BasketItem> products = new HashMap<>();

    public Basket(ProductService productService) {
        this.productService = productService;
    }

    public void addProductId(Long id) {
        var item = products.get(id);
        if (item == null) {
            item = BasketItem.builder()
                             .id(id)
                             .product(productService.getById(id))
                             .build();
            products.put(id, item);
        }
        item.addItem();
    }

    public void removeProduct(Long id) {
        var item = products.get(id);
        item.removeItem();
        if (item.getCount() == 0) {
            products.remove(id);
        }
    }

    public List<BasketItem> getProducts() {
        return new ArrayList<>(products.values());
    }
}
