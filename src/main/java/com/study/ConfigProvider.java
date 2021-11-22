package com.study;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigProvider {
    private static final int DEFAULT_PORT = 8081;
    private static final String DEFAULT_PERSISTENCE = "store-persistence";

    public static int getPort() {
        var portString = System.getenv("PORT");
        try {
            return Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            log.warn("Can't set port from env. Default port is 8080");
        }
        return DEFAULT_PORT;
    }

    public static String getPersistence() {
        var persistence = System.getenv("PORT");
        return persistence != null ? persistence : DEFAULT_PERSISTENCE;
    }
}
