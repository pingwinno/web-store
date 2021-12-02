package com.study.store.security.persistence.user.impl;

import com.study.store.security.model.User;
import com.study.store.security.persistence.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public class HibernateUserRepository implements UserRepository {
    private SessionFactory sessionFactory;

    @Override
    public Optional<User> findByName(String name) {
        try (var session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(User.class, name));
        }
    }

    @Override
    public void save(User product) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try (session) {
            transaction.begin();
            session.persist(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't save entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePassword(User user) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try (session) {
            transaction.begin();
            var loadedEntity = session.load(User.class, user.getName());
            loadedEntity.setPassword(user.getPassword());
            loadedEntity.setSalt(user.getSalt());
            loadedEntity.setRole(user.getRole());
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't update entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String name) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try (session) {
            transaction.begin();
            var product = session.load(User.class, name);
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't delete entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}
