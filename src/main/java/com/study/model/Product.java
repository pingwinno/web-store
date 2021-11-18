package com.study.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double price;
    private Date creationDate;
}
