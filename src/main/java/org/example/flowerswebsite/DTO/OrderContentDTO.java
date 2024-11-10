package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.OrderEntity;
import org.example.flowerswebsite.Entities.ProductEntity;

public class OrderContentDTO {
    private Long quantity;
    private Long orderEntityId;
    private Long productId;
    public OrderContentDTO() {}
    public OrderContentDTO(Long quantity, Long orderEntityId, Long productId) {
        this.quantity = quantity;
        this.orderEntityId = orderEntityId;
        this.productId = productId;
    }

    public Long getOrderEntityId() {
        return orderEntityId;
    }

    public void setOrderEntityId(Long orderEntityId) {
        this.orderEntityId = orderEntityId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
