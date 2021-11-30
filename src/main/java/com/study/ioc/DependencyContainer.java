package com.study.ioc;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {

    private static final Map<Class<?>, Object> DEPENDENCIES = new HashMap<>();


    public static void addDependency(Class<?> clazz, Object dependency) {
        DEPENDENCIES.put(clazz, dependency);
    }

    public static void addDependencies(Map<Class<?>, Object> dependencies) {
        DEPENDENCIES.putAll(dependencies);
    }

    public static <T> T getDependency(Class<T> serviceClass) {
        return serviceClass.cast(DEPENDENCIES.get(serviceClass));
    }
}
