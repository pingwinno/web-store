package com.study.service;

import com.study.exception.NotFoundException;
import com.study.model.Product;
import com.study.persistance.product.ProductRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private final static Product FIRST_PRODUCT = Product.builder()
                                                        .build();
    private final static Product SECOND_PRODUCT = Product.builder()
                                                         .build();
    private final Product productWithXss = Product.builder()
                                                  .name("<script>alert('test');</script>")
                                                  .build();
    private final Product productWithEscapedXss = Product.builder()
                                                         .name("&lt;script&gt;alert('test');&lt;/script&gt;")
                                                         .build();
    private final static List<Product> PRODUCT_LIST = List.of(FIRST_PRODUCT, SECOND_PRODUCT);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    @Test
    void should_returnListOfProducts_when_callGetAllProducts() {
        when(productRepository.findAll()).thenReturn(PRODUCT_LIST);
        assertEquals(PRODUCT_LIST, productService.getAll());
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void should_returnProduct_when_callGetProductWithExistingId() {
        var id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(FIRST_PRODUCT));
        assertEquals(FIRST_PRODUCT, productService.getById(id));
        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void should_throwNoFoundException_when_callGetProductWithExistingId() {
        var id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getById(id), "Product with id 1 not found");
        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
        System.out.println(StringEscapeUtils.escapeHtml4("<script>alert('test');</script>"));
    }

    @Test
    void should_escapeNameWithXssInjection_when_callCreate() {
        when(productRepository.save(productWithEscapedXss)).thenReturn(productWithEscapedXss);
        productService.create(productWithXss);
        verify(productRepository).save(productWithEscapedXss);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void should_escapeNameWithXssInjection_when_callUpdate() {
        when(productRepository.update(productWithEscapedXss)).thenReturn(productWithEscapedXss);
        productService.update(productWithXss);
        verify(productRepository).update(productWithEscapedXss);
        verifyNoMoreInteractions(productRepository);
    }
}