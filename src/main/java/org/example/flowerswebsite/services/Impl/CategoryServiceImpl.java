package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.CategoryType;
import org.example.flowerswebsite.Entities.ProductEntity;
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
    public CategoryDto createCategory(CategoryDto categoryDto){
        CategoryEntity categoryEntity = modelmapper.map(categoryDto, CategoryEntity.class);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        CategoryDto savedCategoryDto = modelmapper.map(savedCategory, CategoryDto.class);
        return savedCategoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));
        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setCategoryType(categoryDto.getType());
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        CategoryDto savedCategoryDto = modelmapper.map(savedEntity, CategoryDto.class);
        return savedCategoryDto;
    }

    @Override
    public CategoryDto findById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        CategoryDto categoryDTO = modelmapper.map(categoryEntity, CategoryDto.class);
        return categoryDTO;
    }
    @Override
    public List<CategoryDto> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categoryEntities.stream()
                .map(categoryEntity -> modelmapper.map(categoryEntity, CategoryDto.class))
                .toList();
        return categoryDtos;
    }
    @Override
    public List<CategoryDto> getByCategoryType(CategoryType type){
        List<CategoryEntity> categoryEntities = categoryRepository.findByCategoryType(type);
        List<CategoryDto> categoryDtos=categoryEntities.stream()
                .map(categoryEntity -> modelmapper.map(categoryEntity,CategoryDto.class))
                .toList();
        return categoryDtos;
    }
}
