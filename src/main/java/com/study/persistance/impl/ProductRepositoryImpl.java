package com.study.persistance.impl;

import com.study.persistance.ProductRepository;
import com.study.persistance.factory.EntityManagerStorage;
import com.study.model.Product;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManagerStorage entityManagerStorage;

    public ProductRepositoryImpl(EntityManagerStorage entityManagerStorage) {
        this.entityManagerStorage = entityManagerStorage;
    }

    @Override
    public List<Product> findAll() {
        var em = entityManagerStorage.getEntityManager();
        try {
            return em.createNamedQuery("product.findAll")
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        var em = entityManagerStorage.getEntityManager();
        try {
            Query query = em.createNamedQuery("product.findById");
            query.setParameter("id", id);
            return query.getResultList()
                        .stream()
                        .findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Product save(Product product) {
        var em = entityManagerStorage.getEntityManager();
        try {
            em.getTransaction()
              .begin();
            em.persist(product);

        } catch (Exception e) {
            em.getTransaction()
              .rollback();
            throw new RuntimeException(e);
        } finally {
            em.getTransaction()
              .commit();
            em.close();
        }
        return product;
    }

    @Override
    public void delete(long id) {
        var em = entityManagerStorage.getEntityManager();
        try {
            em.getTransaction()
              .begin();
            var product = em.find(Product.class, id);
            em.remove(product);
        } catch (Exception e) {
            em.getTransaction()
              .rollback();
            throw new RuntimeException(e);
        } finally {
            em.getTransaction()
              .commit();
            em.close();
        }
    }
}
