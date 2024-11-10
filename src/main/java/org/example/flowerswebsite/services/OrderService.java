package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.OrderDto;
import org.example.flowerswebsite.Entities.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<OrderDto> createOrder(OrderDto orderDto);
    ResponseEntity<OrderDto> updateOrderStatus(Long id, OrderStatus orderStatus);
    ResponseEntity<OrderDto> findById(Long id);
    ResponseEntity<List<OrderDto>> findAllActive();
}
