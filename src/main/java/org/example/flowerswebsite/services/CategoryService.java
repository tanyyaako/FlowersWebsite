package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto);
    ResponseEntity<CategoryDto> updateCategory(CategoryDto categoryDto);
    ResponseEntity<CategoryDto> findById(Long id);

}
