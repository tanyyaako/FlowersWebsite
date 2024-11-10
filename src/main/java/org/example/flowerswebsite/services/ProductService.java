package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ProductDto> createProduct(ProductDto productDto);
    ResponseEntity<ProductDto> update(ProductDto productDto);
    ResponseEntity<List<ProductDto>> getAllByCategory(Long categoryId);
    void delete(Long id);
}
