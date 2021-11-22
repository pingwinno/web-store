package com.study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name = "product.findAll", query = "SELECT p FROM Product p"),
        @NamedQuery(name = "product.findById", query = "SELECT p FROM Product p WHERE p.id = :id")
})
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double price;
    @Column(updatable = false)
    private LocalDate creationDate;

    @PrePersist
    public void setCreationDate() {
        creationDate = LocalDate.now();
    }
}
