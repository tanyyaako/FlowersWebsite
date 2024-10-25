package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="category")
public class CategoryEntity extends BaseEntity {
    private String name;
    private String description;
    private Set<ProductEntity> productEntities;

    public CategoryEntity(String description, String name, Set<ProductEntity> productEntities) {
        this.description = description;
        this.name = name;
        this.productEntities = productEntities;
    }

    protected CategoryEntity() {}

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= ProductEntity.class,mappedBy = "category")
    public Set<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(Set<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}
