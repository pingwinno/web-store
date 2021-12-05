package com.study.store.persistance.product.mapper;

import com.study.store.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                      .id(rs.getLong("ID"))
                      .name(rs.getString("NAME"))
                      .description(rs.getString("DESCRIPTION"))
                      .price(rs.getDouble("PRICE"))
                      .creationDate(rs.getDate("CREATION_DATE")
                                      .toLocalDate())
                      .build();
    }
}
