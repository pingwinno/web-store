package com.study.store.persistance.product.impl;

import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaProductRepository implements ProductRepository {
    private final SessionFactory sessionFactory;

    public JpaProductRepository(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public List<Product> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createNamedQuery("product.findAll", Product.class)
                          .getResultList();
        }
    }

    @Override
    public List<Product> search(String wordForSearch) {
        try (var session = sessionFactory.openSession()) {
            var criteriaBuilder = session.getCriteriaBuilder();
            var criteriaQuery = criteriaBuilder.createQuery(Product.class);
            var root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root).where(
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                                    wordForSearch.toUpperCase()),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
                                    "%" + wordForSearch.toUpperCase() + "%")));
            var query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createNamedQuery("product.findById", Product.class);
            query.setParameter("id", id);
            return query.getResultStream().findFirst();
        }
    }

    @Override
    public void save(Product product) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try {
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
    public void update(Product product) {
        var session = sessionFactory.openSession();
        var transaction = session.getTransaction();
        try (session) {
            transaction.begin();
            var loadedEntity = session.load(Product.class, product.getId());
            loadedEntity.setPrice(product.getPrice());
            loadedEntity.setDescription(product.getDescription());
            loadedEntity.setName(product.getName());
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
            var product = session.load(Product.class, id);
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            log.error("Can't delete entity", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}
