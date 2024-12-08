package org.example.flowerswebsite.Entities;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@Table(name= "userEntity")
public class UserEntity extends BaseEntity {
    private String username;
    private String phoneNumber;
    private String email;
    private UserRoleEnum role;
    private Set<OrderEntity> orderEntities;

    public UserEntity(String email, Set<OrderEntity> orderEntities, String phoneNumber, String username, UserRoleEnum role) {
        this.email = email;
        this.orderEntities = orderEntities;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
    }

    protected UserEntity() {}

    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderEntity.class,mappedBy = "userEntity")
    public Set<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(Set<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
