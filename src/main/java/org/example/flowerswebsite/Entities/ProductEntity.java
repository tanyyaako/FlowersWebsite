package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="product")
public class ProductEntity extends BaseEntity {
    private String name;
    private String description;
    private Double price;
    private boolean isDeleted;
    private CategoryEntity category;
    private Set<StorageContent> storageContents;
    private Set<OrderContent> orderContents;

    protected ProductEntity() {}

    public ProductEntity(CategoryEntity category, String description,
                         boolean isDeleted, String name, Set<OrderContent> orderContents, Double price, Set<StorageContent> storageContents) {
        this.category = category;
        this.description = description;
        this.isDeleted = false;
        this.name = name;
        this.orderContents = orderContents;
        this.price = price;
        this.storageContents = storageContents;
    }

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

    @Column(name = "is_deleted")
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
