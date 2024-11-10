package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.CityEntity;
import org.example.flowerswebsite.Entities.OrderEntity;
import org.example.flowerswebsite.Entities.StorageContent;

import java.util.Set;

public class StorageDto {
    private String adress;
    private Set<StorageContent> storageContents;
    private Set<OrderEntity> orderEntities;
}
