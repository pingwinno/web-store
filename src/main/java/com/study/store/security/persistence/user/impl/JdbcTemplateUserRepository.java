package com.study.store.security.persistence.user.impl;

import com.study.store.security.model.User;
import com.study.store.security.persistence.user.UserRepository;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class JdbcTemplateUserRepository implements UserRepository {
    private final static String SELECT_BY_NAME = "SELECT NAME, PASSWORD, SALT, ROLE FROM USERS WHERE NAME=?;";
    private final static String INSERT = "INSERT INTO USERS (NAME, PASSWORD, SALT, ROLE) Values (? ,? ,? ,?)";
    private final static String UPDATE = "UPDATE USERS SET PASSWORD =?, SALT=? WHERE NAME = ?";
    private final static String DELETE = "DELETE FROM USERS WHERE NAME = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    public JdbcTemplateUserRepository(DataSource dataSource, RowMapper<User> rowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    @SneakyThrows
    @Override
    public Optional<User> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_NAME, rowMapper, name));
    }


    @SneakyThrows
    @Override
    public void save(User product) {
        jdbcTemplate.update(INSERT, product.getName(), product.getPassword(), product.getSalt(),
                product.getRole()
                       .getName());
    }

    @SneakyThrows
    @Override
    public void updatePassword(User user) {
        jdbcTemplate.update(UPDATE, user.getPassword(), user.getSalt(), user.getName());
    }

    @SneakyThrows
    @Override
    public void delete(String name) {
        jdbcTemplate.update(DELETE, name);
    }
}
