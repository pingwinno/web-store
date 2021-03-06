package com.study.store.service;

import com.study.store.exception.NotFoundException;
import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(long id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        MessageFormat.format("Product with id {0} not found", id)));
    }

    public List<Product> getAllByIds(List<Long> ids) {
        return productRepository.findAllByIds(ids);
    }

    public List<Product> search(String query) {
        return productRepository.search(query);
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void update(Product product) {
        productRepository.update(product);
    }

    public void delete(long id) {
        productRepository.delete(id);
    }
}
