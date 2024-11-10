package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.CategoryRepository;
import org.example.flowerswebsite.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelmapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelmapper) {
        this.categoryRepository = categoryRepository;
        this.modelmapper = modelmapper;
    }

    @Override
    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto){
        CategoryEntity categoryEntity = modelmapper.map(categoryDto, CategoryEntity.class);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        CategoryDto savedDto = modelmapper.map(savedCategory, CategoryDto.class);
        ResponseEntity<CategoryDto> responseEntity = ResponseEntity.ok().body(savedDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));
        categoryEntity.setName(categoryDto.getName());
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        CategoryDto savedDTO = modelmapper.map(savedEntity, CategoryDto.class);
        ResponseEntity<CategoryDto> responseEntity = ResponseEntity.ok().body(savedDTO);
        return responseEntity;
    }

    @Override
    public ResponseEntity<CategoryDto> findById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        CategoryDto categoryDTO = modelmapper.map(categoryEntity, CategoryDto.class);
        ResponseEntity<CategoryDto> responseEntity = ResponseEntity.ok().body(categoryDTO);
        return responseEntity;
    }

}
