package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.OrderEntity;

import java.util.Set;

public class UserDto {
    private String username;
    private String phoneNumber;
    private String email;
    private Set<OrderEntity> orderEntities;
}
