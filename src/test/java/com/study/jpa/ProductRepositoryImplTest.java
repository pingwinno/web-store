package com.study.jpa;

import com.study.db.EntityManagerStorage;
import com.study.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRepositoryITest {
    private EntityManagerStorage entityManagerStorage = new EntityManagerStorage("test-store-persistence");
    private final ProductRepository productRepository = new ProductRepositoryImpl(entityManagerStorage);
    private Product product = Product.builder()
                                     .name("book")
                                     .price(9.99)
                                     .creationDate(Date.valueOf(LocalDate.of(2000, 10, 10)))
                                     .build();

   @AfterEach
    void close() {
        entityManagerStorage.close();
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

}