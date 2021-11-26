package com.study.model.enums;

public enum ContextInstance {
    SECURITY_SERVICE("SecurityService"),
    PRODUCT_SERVICE("ProductService"),
    TEMPLATE_PROVIDER("TemplateProvider");

    private final String name;

    ContextInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
