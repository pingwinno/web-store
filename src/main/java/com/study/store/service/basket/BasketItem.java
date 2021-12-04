package com.study.store.service.basket;

import com.study.store.model.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BasketItem {
    private long id;
    private Product product;
    private int count;

    public void addItem() {
        count++;
    }

    public void removeItem() {
        count--;
    }
}
