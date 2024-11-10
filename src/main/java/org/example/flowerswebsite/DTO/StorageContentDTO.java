package org.example.flowerswebsite.DTO;

import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Entities.StorageEntity;

public class StorageContentDTO {
    private Long quantityProduct;
    private Long productId;
    private Long storageId;
    public StorageContentDTO() {}
    public StorageContentDTO(Long quantityProduct, Long productId, Long storageId) {
        this.quantityProduct = quantityProduct;
        this.productId = productId;
        this.storageId = storageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(Long quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }
}
