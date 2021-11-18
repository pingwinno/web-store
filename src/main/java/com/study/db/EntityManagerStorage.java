package com.study.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerStorage {
    private EntityManagerFactory entityManagerFactory;

    public EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("store-persistence");
        }
        return entityManagerFactory.createEntityManager();
    }
}
