package com.study.store.persistance;

import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import com.study.store.persistance.product.impl.JdbcTemplateProductRepository;
import com.study.store.persistance.product.mapper.ProductRowMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcTemplateProductRepositoryTest {

    private final Product product = Product.builder()
                                           .name("book")
                                           .description("This book is awesome.")
                                           .price(9.99)
                                           .build();
    private ProductRepository productRepository;
    private HikariDataSource dataSource;

    @BeforeEach
    void init() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;");
        config.setUsername("sa");
        config.setDriverClassName("org.h2.Driver");
        dataSource = new HikariDataSource(config);
        Flyway flyway = Flyway.configure()
                              .dataSource(dataSource)
                              .load();
        flyway.migrate();
        productRepository = new JdbcTemplateProductRepository(dataSource, new ProductRowMapper());
    }

    @AfterEach
    void destroy() {
        dataSource.close();
    }

    @Test
    void should_returnEmptyList_when_callFindAllOnEmptyDb() {
        System.out.println(productRepository.findAll());
        assertTrue(productRepository.findAll()
                                    .isEmpty());
    }

    @Test
    void should_returnOneRecord_when_saveOneRecordAndFindById() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        var savedProduct = productRepository.findById(1)
                                            .orElse(null);
        assertNotNull(savedProduct);
        assertEquals(1, savedProduct.getId());
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
        var savedProduct = productRepository.findById(1)
                                            .orElse(null);
        assertNotNull(savedProduct);
        productRepository.delete(savedProduct.getId());
        assertTrue(productRepository.findAll()
                                    .isEmpty());
    }

    @Test
    void should_returnProduct_when_saveOneRecordAndSearch() {
        productRepository.save(product);
        assertFalse(productRepository.findAll()
                                     .isEmpty());
        var savedProduct = productRepository.findById(1)
                                            .orElse(null);
        assertNotNull(savedProduct);
        assertTrue(productRepository.search("awesome")
                                    .contains(savedProduct));
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