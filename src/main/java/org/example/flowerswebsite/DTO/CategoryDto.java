package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.ProductEntity;

import java.util.Set;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Set<ProductDto> productEntitieDTOs;

    public CategoryDto() {}

    public CategoryDto(String description, Long id, String name, Set<ProductDto> productEntitieDTOs) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.productEntitieDTOs = productEntitieDTOs;
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

    public Set<ProductDto> getProductEntitieDTOs() {
        return productEntitieDTOs;
    }

    public void setProductEntitieDTOs(Set<ProductDto> productEntitieDTOs) {
        this.productEntitieDTOs = productEntitieDTOs;
    }
}
