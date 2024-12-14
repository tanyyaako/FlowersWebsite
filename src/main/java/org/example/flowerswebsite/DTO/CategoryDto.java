package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.CategoryType;
import org.example.flowerswebsite.Entities.ProductEntity;

import java.util.List;
import java.util.Set;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<Long> productIds;
    private CategoryType type;

    public CategoryDto() {}

    public CategoryDto(String description, Long id, String name, List<Long> productIds,CategoryType type) {
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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
