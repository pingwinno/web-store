package com.study.persistance;

import com.study.model.Product;
import com.study.persistance.product.ProductRepository;
import com.study.persistance.product.impl.JpaProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class JpaProductRepositoryTest {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-store-persistence");
    private final ProductRepository productRepository = new JpaProductRepository(entityManagerFactory);
    private Product product = Product.builder()
                                     .name("book")
                                     .description("This book is awesome.")
                                     .price(9.99)
                                     .build();

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
        product = productRepository.save(product);
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
        product = productRepository.save(product);
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
        product = productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertTrue(productRepository.search("awesome").contains(product));
    }

    @Test
    void should_returnProduct_when_saveOneRecordAndSearchFromStartOfDescription() {
        product = productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertTrue(productRepository.search("this").contains(product));
    }


    @Test
    void should_returnEmptyList_when_saveOneRecordAndSearch() {
        product = productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        assertFalse(productRepository.search("bad").contains(product));
    }
}