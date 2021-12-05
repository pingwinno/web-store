package com.study.store.security.persistence.user.mapper;

import com.study.store.model.enums.Role;
import com.study.store.security.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                   .name(rs.getString("NAME"))
                   .password(rs.getString("PASSWORD"))
                   .salt(rs.getString("SALT"))
                   .role(Role.valueOf(rs.getString("ROLE")))
                   .build();
    }
}
