package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.OrderContent;
import org.example.flowerswebsite.Entities.StorageContent;

import java.util.Set;

public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private Set<StorageContent> storageContents;
    private Set<OrderContent> orderContents;
}
