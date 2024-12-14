package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;
import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private UserRoleEnum name;

    public Role(UserRoleEnum name) {
        this.name = name;
    }

    public Role() {

    }
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    public UserRoleEnum getName() {
        return name;
    }

    public void setName(UserRoleEnum name) {
        this.name = name;
    }
}
