package com.study.model.enums;

public enum Role {
    ADMIN("admin"), USER("user"), GUEST("guest");
    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
