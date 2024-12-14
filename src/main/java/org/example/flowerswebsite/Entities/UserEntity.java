package org.example.flowerswebsite.Entities;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "userEntity")
public class UserEntity extends BaseEntity implements Serializable {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private List<Role> roles;
    private Set<OrderEntity> orderEntities;

    public UserEntity() {
        this.roles = new ArrayList<>();
    }

    public UserEntity(String username, String phoneNumber, String password, Set<OrderEntity> orderEntities, String email) {
        this();
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.orderEntities = orderEntities;
        this.email = email;
    }

    public UserEntity(String username, String encode, String email, String phoneNumber) {
        this.username=username;
        this.password=encode;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }

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

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
