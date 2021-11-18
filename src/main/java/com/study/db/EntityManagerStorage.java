package com.study.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerStorage {
    private final String persistenceName;
    private volatile EntityManagerFactory entityManagerFactory;

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
