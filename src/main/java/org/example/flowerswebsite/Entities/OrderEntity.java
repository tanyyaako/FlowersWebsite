package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class OrderEntity extends BaseEntity {
    private Double orderAmount;
    private UserEntity userEntity;
    private List<OrderContent> orderContents;

    public OrderEntity( UserEntity userEntity) {
        this.orderAmount = 0d;
        this.orderContents = new ArrayList<>();
        this.userEntity = userEntity;
    }

    protected OrderEntity() {}

    @Column(name ="orderAmount")
    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
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

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= UserEntity.class)
    @JoinColumn(name="user_id")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.PERSIST},targetEntity= OrderContent.class,mappedBy = "orderEntity")
    public List<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(List<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }
}
