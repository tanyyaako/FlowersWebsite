package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="product")
public class ProductEntity extends BaseEntity {
    private String name;
    private String description;
    private Double price;
    private Double salePrice;
    private boolean isDeleted;
    private CategoryEntity category;
    private Set<OrderContent> orderContents;
    private Double quantityProduct;
    private String url;

    protected ProductEntity() {}

    public ProductEntity(CategoryEntity category, String description,
                         boolean isDeleted, String name, Set<OrderContent> orderContents,
                         Double price, Double salePrice,Double quantityProduct, String url) {
        this.category = category;
        this.description = description;
        this.isDeleted = false;
        this.name = name;
        this.orderContents = orderContents;
        this.price = price;
        this.salePrice = salePrice;
        this.quantityProduct = quantityProduct;
        this.url = url;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderContent.class,mappedBy = "product")
    public Set<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(Set<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }

    @Column(name = "sale_price")
    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    @Column(name = "quantity")
    public Double getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(Double quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
