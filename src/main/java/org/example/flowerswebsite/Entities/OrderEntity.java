package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="orders")
public class OrderEntity extends BaseEntity {
    private LocalDateTime dateOfCreation;
    private Double orderAmount;
    private OrderStatus status;
    private UserEntity user;
    private StorageEntity storage;
    private Set<OrderContent> orderContents;

    public OrderEntity(LocalDateTime dateOfCreation, Double orderAmount, Set<OrderContent> orderContents,
                       OrderStatus status, StorageEntity storage, UserEntity user) {
        this.dateOfCreation = dateOfCreation;
        this.orderAmount = orderAmount;
        this.orderContents = orderContents;
        this.status = status;
        this.storage = storage;
        this.user = user;
    }

    protected OrderEntity() {}

    @Column(name="dateOfCreation")
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Column(name ="orderAmount")
    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }


    @Enumerated(EnumType.STRING)
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= StorageEntity.class)
    @JoinColumn(name="storage_id")
    public StorageEntity getStorage() {
        return storage;
    }

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= UserEntity.class)
    @JoinColumn(name="user_id")
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderContent.class,mappedBy = "orders")
    public Set<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(Set<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }
}
