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
})
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private double price;
    @Column(name = "CREATION_DATE", updatable = false)
    private LocalDate creationDate;

    @PrePersist
    public void setCreationDate() {
        creationDate = LocalDate.now();
    }
}
