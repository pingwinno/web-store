package com.study.model;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class EntityManagerTest {

    @Test
    protected void configurationTest() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-store-persistence");
        entityManagerFactory.close();
    }
}