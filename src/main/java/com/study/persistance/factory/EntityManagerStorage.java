package com.study.persistance.factory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerStorage implements AutoCloseable {
    private final String persistenceName;
    private volatile javax.persistence.EntityManagerFactory entityManagerFactory;

    public EntityManagerStorage(String persistenceName) {
        this.persistenceName = persistenceName;
    }

    public synchronized EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceName);
        }
        return entityManagerFactory.createEntityManager();
    }

    public synchronized void close() {
        entityManagerFactory.close();
    }
}
