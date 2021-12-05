package com.study.store.persistance.product.impl;

import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Repository
public class JdbcTemplateProductRepository implements ProductRepository {
    private final static String SELECT_ALL = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT;";
    private final static String SELECT_BY_ID = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE ID=?;";
    private final static String SELECT_BY_IDS = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE ID IN ";
    private final static String FIND_BY_NAME_AND_DESCRIPTION = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE NAME=? OR DESCRIPTION LIKE ?;";
    private final static String INSERT = "INSERT INTO PRODUCT (NAME, DESCRIPTION, PRICE, CREATION_DATE) Values (? ,? ,? ,?)";
    private final static String UPDATE = "UPDATE PRODUCT SET NAME = ?,DESCRIPTION =?, PRICE=? WHERE ID = ?";
    private final static String DELETE = "DELETE FROM PRODUCT WHERE ID = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> rowMapper;

    public JdbcTemplateProductRepository(DataSource dataSource, RowMapper<Product> rowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        var stringJoiner = new StringJoiner(",", " (", ");");
        for (int i = 0; i < ids.size(); i++) {
            stringJoiner.add("?");
        }
        var queryString = SELECT_BY_IDS + stringJoiner;
        return jdbcTemplate.queryForList(queryString, Product.class, ids);
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
    }

    @Override
    public List<Product> search(String query) {
        return jdbcTemplate.query(FIND_BY_NAME_AND_DESCRIPTION, rowMapper, query, "%" + query + "%");
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(INSERT, product.getName(), product.getDescription(), product.getPrice(),
                LocalDate.now());
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(UPDATE, product.getName(), product.getDescription(), product.getPrice(), product.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE, id);
    }
}
