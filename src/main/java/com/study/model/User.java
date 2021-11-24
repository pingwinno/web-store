package com.study.model;

import com.study.model.enums.Role;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "USERS")
public class User {
    @Id
    private String name;
    private String password;
    private String salt;
    private Role role;
}
