package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.CategoryRepository;
import org.example.flowerswebsite.Repositories.ProductRepository;
import org.example.flowerswebsite.Repositories.StorageRepository;
import org.example.flowerswebsite.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final StorageRepository storageRepository;

    public ProductServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper,
                              ProductRepository productRepository, StorageRepository storageRepository) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
    }

    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        CategoryEntity categoryEntityId = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow();
        productEntity.setCategory(categoryEntityId);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        ProductDto savedProductDto = modelMapper.map(savedProductEntity, ProductDto.class);
        ResponseEntity<ProductDto> responseEntity = ResponseEntity.ok().body(savedProductDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<ProductDto> update(ProductDto productDto) {
        ProductEntity productEntity = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if (productDto.getCategoryId() != null ) {
            CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            productEntity.setCategory(categoryEntity);
        }
        if (productDto.getName() != null) {
            productEntity.setName(productDto.getName());
        }
        if (productDto.getPrice() != null) {
            productEntity.setPrice(productDto.getPrice());
        }
        if (productDto.getPrice() != null) {
            productEntity.setPrice(productDto.getPrice());
        }
        if (productDto.getDescription() != null) {
            productEntity.setDescription(productDto.getDescription());
        }
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        ProductDto savedProductDto = modelMapper.map(savedProductEntity, ProductDto.class);
        ResponseEntity<ProductDto> responseEntity = ResponseEntity.ok().body(savedProductDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllByCategory(Long categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " not found"));
        List<ProductEntity> productEntities = productRepository.findAllByCategoryAndIsDeletedFalse(categoryEntity);
        List<ProductDto> productDTOs = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        ResponseEntity<List<ProductDto>> responseEntity = ResponseEntity.ok().body(productDTOs);
        return responseEntity;
    }
    @Override
    public void delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productEntity.setDeleted(true);
        productRepository.save(productEntity);
    }

}
