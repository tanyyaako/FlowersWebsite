package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.ProductEntity;

import java.util.List;
import java.util.Set;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<Long> productIds;

    public CategoryDto() {}

    public CategoryDto(String description, Long id, String name, List<Long> productIds) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.productIds = productIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
