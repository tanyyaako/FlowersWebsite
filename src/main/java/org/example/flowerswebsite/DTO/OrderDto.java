package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.OrderContent;
import org.example.flowerswebsite.Entities.OrderStatus;
import org.example.flowerswebsite.Entities.StorageEntity;
import org.example.flowerswebsite.Entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDto {
    private LocalDateTime dateOfCreation;
    private Double orderAmount;
    private OrderStatus status;
    private Long userEntityId;
    private Long storageId;
    private Set<OrderContentDTO> orderContentDTOs;

    public OrderDto() {}

    public OrderDto(LocalDateTime dateOfCreation, Double orderAmount, Set<OrderContentDTO> orderContentDTOs, OrderStatus status, Long storageId, Long userEntityId) {
        this.dateOfCreation = dateOfCreation;
        this.orderAmount = orderAmount;
        this.orderContentDTOs = orderContentDTOs;
        this.status = status;
        this.storageId = storageId;
        this.userEntityId = userEntityId;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Set<OrderContentDTO> getOrderContentDTOs() {
        return orderContentDTOs;
    }

    public void setOrderContentDTOs(Set<OrderContentDTO> orderContentDTOs) {
        this.orderContentDTOs = orderContentDTOs;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getUserEntityId() {
        return userEntityId;
    }

    public void setUserEntityId(Long userEntityId) {
        this.userEntityId = userEntityId;
    }
}
