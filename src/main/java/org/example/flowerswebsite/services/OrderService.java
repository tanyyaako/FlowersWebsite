package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.Entities.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrderStatus(Long id, OrderStatus orderStatus);
    OrderDto findById(Long id);
    List<OrderDto> findAllActive();
}
