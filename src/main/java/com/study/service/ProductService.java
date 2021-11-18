package com.study.service;

import com.study.exception.NotFoundException;
import com.study.model.Product;
import com.study.persistance.ProductRepository;
import com.study.util.ProductUtil;

import java.text.MessageFormat;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(MessageFormat
                                        .format("Product with id {0} not found", id)));
    }

    public void createProduct(Product product) {
        ProductUtil.escapeProduct(product);
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        ProductUtil.escapeProduct(product);
        productRepository.update(product);
    }

    public void deleteProduct(long id) {
        productRepository.delete(id);
    }
}
