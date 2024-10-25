package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "orderContent")
public class OrderContent extends BaseEntity{
    private Long quantity;
    private OrderEntity order;
    private ProductEntity product;

    public OrderContent(OrderEntity order, ProductEntity product, Long quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    protected OrderContent() {}

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= OrderEntity.class)
    @JoinColumn(name = "order_id")
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= ProductEntity.class)
    @JoinColumn(name = "product_id")
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Column(name="quantity")
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
