package com.study;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ConfigProvider {
    public static final String URL = "hibernate.connection.url";
    public static final String USER = "hibernate.connection.username";
    public static final String PASSWORD = "hibernate.connection.password";
    public static final String PORT = "server.port";
    public static final String PERSISTENCE = "store-persistence";
    public static final String DRIVER = "hibernate.connection.driver_class";

    private final Properties properties;

    @SneakyThrows
    public ConfigProvider() {
        properties = new Properties();
        properties.load(Thread.currentThread()
                              .getContextClassLoader()
                              .getResourceAsStream("application.properties"));
    }

    public void populateDriverFromEnv() {
        properties.setProperty(URL, getDbUrl());
        properties.setProperty(USER, getDbUser());
        properties.setProperty(PASSWORD, getDbPassword());
        properties.setProperty(DRIVER, getDbDriver());
    }

    public int getPort() {
        var portString = System.getenv("PORT");
        try {
            return Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            log.warn("Can't set port from env. Default port is 8080");
        }
        return Integer.parseInt(properties.getProperty(PORT));
    }

    public String getDbUrl() {
        var url = System.getenv("DB_URL");
        return url != null ? url : properties.getProperty(URL);
    }

    public String getDbUser() {
        var user = System.getenv("DB_USER");
        return user != null ? user : properties.getProperty(USER);
    }

    public String getDbPassword() {
        var password = System.getenv("DB_PASSWORD");
        return password != null ? password : properties.getProperty(PASSWORD);
    }

    public String getDbDriver() {
        var driver = System.getenv("DB_DRIVER");
        return driver != null ? driver : properties.getProperty(DRIVER);
    }

    public Properties getProperties() {
        return properties;
    }
}
