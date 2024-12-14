package org.example.flowerswebsite.DTO;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDto {
    private Double orderAmount;
    private Long userEntityId;
    private Set<OrderContentDTO> orderContentDTOs;

    public OrderDto() {}

    public OrderDto(Double orderAmount, Set<OrderContentDTO> orderContentDTOs, Long userEntityId) {
        this.orderAmount = orderAmount;
        this.orderContentDTOs = orderContentDTOs;
        this.userEntityId = userEntityId;
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

    public Long getUserEntityId() {
        return userEntityId;
    }

    public void setUserEntityId(Long userEntityId) {
        this.userEntityId = userEntityId;
    }
}
