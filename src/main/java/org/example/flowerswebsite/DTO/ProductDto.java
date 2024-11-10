package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.OrderContent;
import org.example.flowerswebsite.Entities.StorageContent;

import java.util.Set;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;

    public ProductDto(Long categoryId, String description, String name, Double price, Long id) {
        this.categoryId = categoryId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.id = id;
    }
    public ProductDto() {}

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
