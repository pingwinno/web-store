package com.study.persistance.user.impl;

import com.study.model.Product;
import com.study.model.User;
import com.study.persistance.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaUserRepository implements UserRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaUserRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    public Optional<User> findByName(String name) {
        var em = entityManagerFactory.createEntityManager();
        try {
            Query query = em.createNamedQuery("users.findByName");
            query.setParameter("name", name);
            return query.getResultList()
                        .stream()
                        .findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public User save(User user) {
        var em = entityManagerFactory.createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't save entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public User update(User user) {
        var em = entityManagerFactory.createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't update entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return user;
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
