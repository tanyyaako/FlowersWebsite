package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto);
    CategoryDto findById(Long id);
    List<CategoryDto> getAll();

}
