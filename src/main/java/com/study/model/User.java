package com.study.model;

import com.study.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name = "users.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
})
@Table(name = "USERS")
public class User {
    @Id
    private Long id;
    private String name;
    private String password;
    private String salt;
    private Role role;
}
