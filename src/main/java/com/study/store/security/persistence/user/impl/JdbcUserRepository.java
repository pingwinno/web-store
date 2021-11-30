package com.study.store.security.persistence.user.impl;

import com.study.di.ServiceLocator;
import com.study.store.model.enums.Role;
import com.study.store.security.model.User;
import com.study.store.security.persistence.user.UserRepository;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {
    private final static String SELECT_BY_NAME = "SELECT NAME, PASSWORD, SALT, ROLE FROM USERS WHERE NAME=?;";
    private final static String INSERT = "INSERT INTO USERS (NAME, PASSWORD, SALT, ROLE) Values (? ,? ,? ,?)";
    private final static String UPDATE = "UPDATE USERS SET NAME = ?,PASSWORD =?, SALT=? WHERE ROLE = ?";
    private final static String DELETE = "DELETE FROM USERS WHERE NAME = ?";
    private final DataSource dataSource = ServiceLocator.getDependency(DataSource.class);


    @SneakyThrows
    @Override
    public Optional<User> findByName(String name) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_BY_NAME)) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            var users = new ArrayList<User>();
            while (resultSet.next()) {
                var product = User.builder()
                                  .name(resultSet.getString("NAME"))
                                  .password(resultSet.getString("PASSWORD"))
                                  .salt(resultSet.getString("SALT"))
                                  .role(Role.values()[resultSet.getInt("ROLE")])
                                  .build();
                users.add(product);
            }
            resultSet.close();
            return users.stream()
                        .findFirst();
        }
    }


    @SneakyThrows
    @Override
    public void save(User product) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPassword());
            preparedStatement.setString(3, product.getSalt());
            preparedStatement.setInt(4, product.getRole()
                                               .ordinal());
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    @Override
    public void updatePassword(User user) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    @Override
    public void delete(String name) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }
}
