package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.OrderEntity;

import java.util.Set;

public class UserDto {
    private String username;
    private String phoneNumber;
    private String email;
    private Set<OrderDto> orderEntitieDtos;

    public UserDto() {}
    public UserDto(String username, String phoneNumber, String email, Set<OrderDto> orderEntitieDtos) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.orderEntitieDtos = orderEntitieDtos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<OrderDto> getOrderEntitieDtos() {
        return orderEntitieDtos;
    }

    public void setOrderEntitieDtos(Set<OrderDto> orderEntitieDtos) {
        this.orderEntitieDtos = orderEntitieDtos;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
