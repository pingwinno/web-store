package com.study.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
public class Product implements Serializable {
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
