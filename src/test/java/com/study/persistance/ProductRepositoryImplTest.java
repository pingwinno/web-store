package com.study.persistance;

import com.study.model.Product;
import com.study.persistance.factory.EntityManagerStorage;
import com.study.persistance.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductRepositoryImplTest {
    private EntityManagerStorage entityManagerStorage = new EntityManagerStorage("test-store-persistence");
    private final ProductRepository productRepository = new ProductRepositoryImpl(entityManagerStorage);
    private Product product = Product.builder()
                                     .name("book")
                                     .price(9.99)
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

}