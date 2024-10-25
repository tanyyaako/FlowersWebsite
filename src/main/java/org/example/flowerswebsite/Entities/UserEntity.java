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
    private Set<OrderEntity> order;

    public UserEntity(String email, Set<OrderEntity> order, String phoneNumber, String username) {
        this.email = email;
        this.order = order;
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
        return order;
    }

    public void setOrder(Set<OrderEntity> order) {
        this.order = order;
    }
}
