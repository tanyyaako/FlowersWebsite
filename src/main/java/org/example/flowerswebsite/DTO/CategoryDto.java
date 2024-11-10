package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.ProductEntity;

import java.util.Set;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Set<ProductEntity> productEntities;

    public CategoryDto() {}

    public CategoryDto(String description, Long id, String name, Set<ProductEntity> productEntities) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.productEntities = productEntities;
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

    public Set<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(Set<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}
