package com.study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name = "product.findAll",query = "SELECT p FROM Product p"),
        @NamedQuery(name = "product.findById",query = "SELECT p FROM Product p WHERE p.id = :id")
})
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double price;
    private Date creationDate;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @PrePersist
    public void setCreationDate() {
        creationDate = Date.valueOf(LocalDate.now());
    }
}
