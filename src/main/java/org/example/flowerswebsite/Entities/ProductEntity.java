package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="product")
public class ProductEntity extends BaseEntity {
    private String name;
    private String description;
    private Double price;
    private CategoryEntity category;
    private Set<StorageContent> storageContents;
    private Set<OrderContent> orderContents;

    public ProductEntity(CategoryEntity category, String description, String name,
                         Set<OrderContent> orderContents, Double price, Set<StorageContent> storageContents) {
        this.category = category;
        this.description = description;
        this.name = name;
        this.orderContents = orderContents;
        this.price = price;
        this.storageContents = storageContents;
    }

    protected ProductEntity() {}

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= CategoryEntity.class)
    @JoinColumn(name = "category_id")
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= StorageContent.class,mappedBy = "product")
    public Set<StorageContent> getStorageContents() {
        return storageContents;
    }

    public void setStorageContents(Set<StorageContent> storageContents) {
        this.storageContents = storageContents;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderContent.class,mappedBy = "product")
    public Set<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(Set<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }
}
