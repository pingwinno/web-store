package com.study.store.persistance;

import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import com.study.store.persistance.product.impl.HibernateProductRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HibernateProductRepositoryTest {

    private final Product product = Product.builder()
                                           .name("book")
                                           .description("This book is awesome.")
                                           .price(9.99)
                                           .build();
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
            "test-store-persistence");
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository = new HibernateProductRepository(entityManagerFactory.unwrap(SessionFactory.class));
    }

    @AfterEach
    void close() {
        entityManagerFactory.close();
    }

    @Test
    void should_returnEmptyList_when_callFindAllOnEmptyDb() {
        assertTrue(productRepository.findAll()
                                    .isEmpty());
    }

    @Test
    void should_returnOneRecord_when_saveOneRecordAndFindById() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertEquals(product, productRepository.findById(product.getId())
                                               .orElse(null));
    }

    @Test
    void should_returnUpdatedRecord_when_saveAndUpdateOneRecordAndFindById() {
        var expectedId = 1L;
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        var savedProduct = productRepository.findById(expectedId)
                                            .orElse(null);
        assertNotNull(savedProduct);
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        savedProduct.setPrice(20.99);
        productRepository.update(savedProduct);
        var updatedProduct = productRepository.findById(expectedId)
                                              .orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(savedProduct.getPrice(), updatedProduct.getPrice());
    }

    @Test
    void should_returnEmptyList_when_saveOneRecordAndDeleteById() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertEquals(product, productRepository.findById(product.getId())
                                               .orElse(null));
        productRepository.delete(product.getId());
        assertTrue(productRepository.findAll()
                                    .isEmpty());
    }

    @Test
    void should_returnProduct_when_saveOneRecordAndSearch() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertTrue(productRepository.search("awesome")
                                    .contains(product));
    }

    @Test
    void should_returnProduct_when_saveOneRecordAndSearchFromStartOfDescription() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertTrue(productRepository.search("this")
                                    .contains(product));
    }


    @Test
    void should_returnEmptyList_when_saveOneRecordAndSearch() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertFalse(productRepository.search("bad")
                                     .contains(product));
    }
}