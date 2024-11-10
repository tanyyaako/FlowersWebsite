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
    private Set<OrderContent> orderContents;

}
