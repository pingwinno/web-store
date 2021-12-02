package com.study.store.persistance.product.impl;

import com.study.store.model.Product;
import com.study.store.persistance.product.ProductRepository;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class JdbcProductRepository implements ProductRepository {
    private final static String SELECT_ALL = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT;";
    private final static String SELECT_BY_ID = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE ID=?;";
    private final static String SELECT_BY_IDS = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE ID IN (%s);";
    private final static String FIND_BY_NAME_AND_DESCRIPTION = "SELECT ID, NAME, DESCRIPTION, PRICE, CREATION_DATE FROM PRODUCT WHERE NAME=? OR DESCRIPTION LIKE ?;";
    private final static String INSERT = "INSERT INTO PRODUCT (NAME, DESCRIPTION, PRICE, CREATION_DATE) Values (? ,? ,? ,?)";
    private final static String UPDATE = "UPDATE PRODUCT SET NAME = ?,DESCRIPTION =?, PRICE=? WHERE ID = ?";
    private final static String DELETE = "DELETE FROM PRODUCT WHERE ID = ?";
    private DataSource dataSource;

    @SneakyThrows
    @Override
    public List<Product> findAll() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_ALL);
             var resultSet = statement.executeQuery()) {

            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = Product.builder()
                                     .id(resultSet.getLong("ID"))
                                     .name(resultSet.getString("NAME"))
                                     .description(resultSet.getString("DESCRIPTION"))
                                     .price(resultSet.getDouble("PRICE"))
                                     .creationDate(resultSet.getDate("CREATION_DATE")
                                                            .toLocalDate())
                                     .build();
                products.add(product);
            }
            return products;
        }
    }

    @SneakyThrows
    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        var stringJoiner = new StringJoiner(",");
        for (int i = 0; i < ids.size(); i++) {
            stringJoiner.add("?");
        }
        var queryString = stringJoiner.toString();
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(queryString)) {
            for (int i = 1; i <= ids.size(); i++) {
                statement.setLong(i, ids.get(i));
            }
            var resultSet = statement.executeQuery();
            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = Product.builder()
                                     .id(resultSet.getLong("ID"))
                                     .name(resultSet.getString("NAME"))
                                     .description(resultSet.getString("DESCRIPTION"))
                                     .price(resultSet.getDouble("PRICE"))
                                     .creationDate(resultSet.getDate("CREATION_DATE")
                                                            .toLocalDate())
                                     .build();
                products.add(product);
            }
            resultSet.close();
            return products;
        }
    }


    @SneakyThrows
    @Override
    public Optional<Product> findById(long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = Product.builder()
                                     .id(resultSet.getLong("ID"))
                                     .name(resultSet.getString("NAME"))
                                     .description(resultSet.getString("DESCRIPTION"))
                                     .price(resultSet.getDouble("PRICE"))
                                     .creationDate(resultSet.getDate("CREATION_DATE")
                                                            .toLocalDate())
                                     .build();
                products.add(product);
            }
            resultSet.close();
            return products.stream()
                           .findFirst();
        }
    }

    @SneakyThrows
    @Override
    public List<Product> search(String query) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(FIND_BY_NAME_AND_DESCRIPTION)) {
            statement.setString(1, query);
            statement.setString(2, "%" + query + "%");
            var resultSet = statement.executeQuery();
            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = Product.builder()
                                     .id(resultSet.getLong("ID"))
                                     .name(resultSet.getString("NAME"))
                                     .description(resultSet.getString("DESCRIPTION"))
                                     .price(resultSet.getDouble("PRICE"))
                                     .creationDate(resultSet.getDate("CREATION_DATE")
                                                            .toLocalDate())
                                     .build();
                products.add(product);
            }
            resultSet.close();
            return products;
        }
    }

    @SneakyThrows
    @Override
    public void save(Product product) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    @Override
    public void update(Product product) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setLong(4, product.getId());
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    @Override
    public void delete(long id) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
