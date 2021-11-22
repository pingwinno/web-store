package com.study.model;

import lombok.*;

import javax.persistence.*;
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
        @NamedQuery(name = "product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
        @NamedQuery(name = "product.search", query = "SELECT p FROM Product p WHERE p.name LIKE :name OR p.description LIKE :description")
})
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private double price;
    @Column(updatable = false)
    private LocalDate creationDate;

    @PrePersist
    public void setCreationDate() {
        creationDate = LocalDate.now();
    }
}
