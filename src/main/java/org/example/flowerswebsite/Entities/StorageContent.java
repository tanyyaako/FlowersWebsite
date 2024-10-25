package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="storageContent")
public class StorageContent extends BaseEntity{
    private Long quantityProduct;
    private ProductEntity product;
    private StorageEntity storage;

    public StorageContent(ProductEntity product, Long quantityProduct, StorageEntity storage) {
        this.product = product;
        this.quantityProduct = quantityProduct;
        this.storage = storage;
    }
    protected StorageContent() {}

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= ProductEntity.class)
    @JoinColumn(name="prosuct_id")
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Column(name="quantity")
    public Long getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(Long quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REFRESH,targetEntity= StorageEntity.class)
    @JoinColumn(name="storage_id")
    public StorageEntity getStorage() {
        return storage;
    }

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
    }
}
