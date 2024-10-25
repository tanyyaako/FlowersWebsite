package org.example.flowerswebsite.Entities;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@Table(name= "user")
public class UserEntity extends BaseEntity {
    private String username;
    private String phoneNumber;
    private String email;
    private Set<OrderEntity> orders;

    public UserEntity(String email, Set<OrderEntity> orders, String phoneNumber, String username) {
        this.email = email;
        this.orders = orders;
        this.phoneNumber = phoneNumber;
        this.username = username;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderEntity.class,mappedBy = "user")
    public Set<OrderEntity> getOrder() {
        return orders;
    }

    public void setOrder(Set<OrderEntity> orders) {
        this.orders = orders;
    }
}
