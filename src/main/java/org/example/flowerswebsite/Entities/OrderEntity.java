package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class OrderEntity extends BaseEntity {
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfCompletion;
    private Double orderAmount;
    private OrderStatus status;
    private UserEntity userEntity;
    private StorageEntity storage;
    private List<OrderContent> orderContents;

    public OrderEntity(StorageEntity storage, UserEntity userEntity) {
        this.dateOfCreation = LocalDateTime.now();
        this.orderAmount = 0d;
        this.orderContents = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.storage = storage;
        this.userEntity = userEntity;
        this.dateOfCompletion = dateOfCompletion;
    }

    protected OrderEntity() {}

    @Column(name="dateOfCreation")
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Column(name="dateOfCompletion")
    public LocalDateTime getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(LocalDateTime dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
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

    public void setOrderContent(ProductEntity productEntity, Long quantity) {
        OrderContent orderContent = new OrderContent(
                this,
                productEntity,
                quantity
        );
        orderContents.add(orderContent);
        this.orderAmount += productEntity.getPrice() * orderContent.getQuantity();
        this.orderAmount = Math.floor(orderAmount * 100) / 100;
    }
    public void changeStatus(OrderStatus orderStatus) {
        if (status.equals(OrderStatus.CREATED)) {
            switch (orderStatus) {
                case COMPLETED -> {
                    this.dateOfCompletion = LocalDateTime.now();
                    this.status = OrderStatus.COMPLETED;
                }
                case CANCELED -> {
                    this.status = OrderStatus.CANCELED;
                }
            }
        }
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
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderContent.class,mappedBy = "orderEntity")
    public List<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(List<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }
}
