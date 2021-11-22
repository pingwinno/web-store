package com.study.persistance.impl;

import com.study.model.Product;
import com.study.persistance.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Slf4j
public class JpaProductRepository implements ProductRepository {
    private static final String SQL_WILDCARD_TEMPLATE = "%{0}%";
    private final EntityManagerFactory entityManagerFactory;

    public JpaProductRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Product> findAll() {
        var em = entityManagerFactory.createEntityManager();
        try {
            return em.createNamedQuery("product.findAll")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> search(String wordForSearch) {
        var em = entityManagerFactory.createEntityManager();
        try {
            var wordWithWildcards = format(SQL_WILDCARD_TEMPLATE, wordForSearch);
            Query query = em.createNamedQuery("product.search");
            query.setParameter("name", wordWithWildcards);
            query.setParameter("description", wordWithWildcards);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        var em = entityManagerFactory.createEntityManager();
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
        var em = entityManagerFactory.createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't save entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        var em = entityManagerFactory.createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't update entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return product;
    }

    @Override
    public void delete(long id) {
        var em = entityManagerFactory.createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            var product = em.find(Product.class, id);
            em.remove(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't delete entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
