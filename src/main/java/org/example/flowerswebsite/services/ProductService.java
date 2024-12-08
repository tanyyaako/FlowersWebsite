package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getById(Long id);
    ProductDto update(ProductDto productDto);
    List<ProductDto> getAllByCategory(Long categoryId);
    void delete(Long id);
    void setSale(Long productID ,Double sale);
    List<ProductDto> getSaleProducts();
    void deleteSale(Long productID);
    List<ProductDto> getTopSelling();
    List<ProductDto> getByCategoriesOrPrice(List<CategoryDto> categoryDtos, Double priceFrom, Double priceTo);
    List<ProductDto> getAll();
    List<ProductDto> getAllNotDeleted();
    Double getSaleOfProduct(Long productID);
}
