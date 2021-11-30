package com.study.store.model.enums;

public enum ContextInstance {
    SECURITY_SERVICE("SecurityService"),
    PRODUCT_SERVICE("ProductService"),
    TEMPLATE_PROVIDER("TemplateProvider"),
    TOKEN_STORAGE("TokenStorage"),
    BASKET_SERVICE("BasketService");

    private final String name;

    ContextInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
