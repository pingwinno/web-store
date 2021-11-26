package com.study.persistance.user.impl;

import com.study.model.User;
import com.study.persistance.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Slf4j
public class JpaUserRepository implements UserRepository {
    private final SessionFactory sessionFactory;

    public JpaUserRepository(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

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
    public void delete(long id) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try (session) {
            transaction.begin();
            var product = session.load(User.class, id);
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't delete entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}
