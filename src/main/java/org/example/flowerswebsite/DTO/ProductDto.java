package org.example.flowerswebsite.DTO;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryID;
    private Double salePrice;
    private Double quantityProduct;
    private String url;

    public ProductDto(Long categoryID, String description, String name, Double price,
                      Long id, Double salePrice, Double quantityProduct, String url) {
        this.categoryID = categoryID;
        this.description = description;
        this.name = name;
        this.price = price;
        this.id = id;
        this.salePrice = salePrice;
        this.quantityProduct = quantityProduct;
        this.url = url;
    }
    public ProductDto() {}

    public Long getCategoryId() {
        return categoryID;
    }

    public void setCategoryId(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(Double quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
