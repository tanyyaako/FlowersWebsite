package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.OrderDto;

import java.util.List;

public interface OrderService {
    void createOrder(String userId);
    OrderDto findById(Long id);
    List<OrderDto> findAll();
}
