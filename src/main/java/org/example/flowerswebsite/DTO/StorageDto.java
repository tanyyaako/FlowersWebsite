package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.CityEntity;
import org.example.flowerswebsite.Entities.OrderEntity;
import org.example.flowerswebsite.Entities.StorageContent;

import java.util.Set;

public class StorageDto {
    private Long id;
    private String adress;
    private Set<StorageContentDTO> storageContentDTOs;
    private Set<OrderDto> orderEntitieDTOs;
    public StorageDto() {}

    public StorageDto(String adress, Long id, Set<OrderDto> orderEntitieDTOs, Set<StorageContentDTO> storageContentDTOs) {
        this.adress = adress;
        this.id = id;
        this.orderEntitieDTOs = orderEntitieDTOs;
        this.storageContentDTOs = storageContentDTOs;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Set<OrderDto> getOrderEntitieDTOs() {
        return orderEntitieDTOs;
    }

    public void setOrderEntitieDTOs(Set<OrderDto> orderEntitieDTOs) {
        this.orderEntitieDTOs = orderEntitieDTOs;
    }

    public Set<StorageContentDTO> getStorageContentDTOs() {
        return storageContentDTOs;
    }

    public void setStorageContentDTOs(Set<StorageContentDTO> storageContentDTOs) {
        this.storageContentDTOs = storageContentDTOs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
