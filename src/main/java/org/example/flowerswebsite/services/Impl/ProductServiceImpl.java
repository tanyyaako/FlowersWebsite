package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.CategoryRepository;
import org.example.flowerswebsite.Repositories.OrderContentRepository;
import org.example.flowerswebsite.Repositories.ProductRepository;
import org.example.flowerswebsite.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final OrderContentRepository orderContentRepository;

    public ProductServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper,
                              ProductRepository productRepository,
                              OrderContentRepository orderContentRepository) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.orderContentRepository=orderContentRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        CategoryEntity categoryEntityId = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        productEntity.setCategory(categoryEntityId);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        ProductDto savedProductDto = modelMapper.map(savedProductEntity, ProductDto.class);
        return savedProductDto;
    }

    @Override
    public ProductDto getById(Long id){
        ProductEntity productEntity = productRepository.findById(id).orElseThrow();
        ProductDto savedProductDto = modelMapper.map(productEntity, ProductDto.class);
        return savedProductDto;
    }

    @Override
    public ProductDto update(ProductDto productDto) {
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
        return savedProductDto;
    }

    @Override
    public List<ProductDto> getAllByCategory(Long categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " not found"));
        List<ProductEntity> productEntities = productRepository.findAllByCategoryAndIsDeletedFalse(categoryEntity);
        List<ProductDto> productDTOs = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        return productDTOs;
    }


    @Override
    public void delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productEntity.setDeleted(true);
        productRepository.save(productEntity);
    }

    @Override
    public void setSale(Long productID ,Double sale){
        ProductEntity productEntity = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productID + " not found"));
        Double oldPrice = productEntity.getPrice();
        Double newPrice = oldPrice-(oldPrice*sale/100);
        productEntity.setSalePrice(newPrice);
        productRepository.save(productEntity);
    }
    @Override
    public Double getSaleOfProduct(Long productID){
        ProductEntity productEntity = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productID + " not found"));
        Double price = productEntity.getPrice();
        Double salePrice = productEntity.getSalePrice();
        if (price == null || salePrice == null || price <= 0) {
            return 0.0;
        }
        Double sale = ((price - salePrice) / price) * 100;
        return Math.round(sale * 100.0) / 100.0;
    }

    @Override
    public List<ProductDto> getSaleProducts(){
        List<ProductEntity> productEntities = productRepository.findAllBySale();
        System.out.println(productEntities);
        List<ProductDto> productDTOs = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        System.out.println(productDTOs);
        return productDTOs;
    }

    @Override
    public void deleteSale(Long productID){
        ProductEntity productEntity = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productID + " not found"));
        productEntity.setSalePrice(null);
        productRepository.save(productEntity);
    }

    @Override
    public List<ProductDto> getTopSelling(){
        List<Object[]> results = orderContentRepository.findTopSellingProducts();
        List<ProductEntity > topProducts =results.stream()
                .map(res -> (ProductEntity) res[0])
                .limit(10)
                .toList();

        List<ProductDto> productDTOs = topProducts.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        return productDTOs;
    }

    @Override
    public List<ProductDto> getByCategoriesOrPrice(List<CategoryDto> categoryDtos, Double priceFrom, Double priceTo,String name) {
        List<CategoryEntity> categoryEntities = null;
        if (categoryDtos != null && !categoryDtos.isEmpty()) {
            categoryEntities = categoryDtos.stream()
                    .map(categoryDto -> modelMapper.map(categoryDto, CategoryEntity.class))
                    .toList();
        }
        List<ProductEntity> productEntities;
        if (name != null) {
            if (categoryDtos != null && !categoryDtos.isEmpty() && priceFrom != null && priceTo != null) {
                productEntities = productRepository.findByCategoriesInAndPriceBetweenAndNameContainingIgnoreCase(
                        categoryEntities, priceFrom, priceTo, name);
            } else if (categoryDtos != null && !categoryDtos.isEmpty()) {
                productEntities = productRepository.findByCategoriesAndNameContainingIgnoreCase(categoryEntities, name);
            } else if (priceFrom != null && priceTo != null) {
                productEntities = productRepository.findAllByPriceBetweenAndNameContainingIgnoreCase(priceFrom, priceTo, name);
            } else if (priceFrom != null) {
                productEntities = productRepository.findByPriceGreaterThanEqualAndNameContainingIgnoreCase(priceFrom, name);
                System.out.println("Calling name and pricefrom method!!!!!!");
            } else if (priceTo != null) {
                productEntities = productRepository.findByPriceLessThanEqualAndNameContainingIgnoreCase(priceTo, name);
            } else {
                productEntities = productRepository.findByNameContainingIgnoreCase(name);
            }
        } else {
            if (categoryDtos != null && !categoryDtos.isEmpty() && priceFrom != null && priceTo != null) {
                productEntities = productRepository.findByCategoriesInAndPriceBetween(categoryEntities, priceFrom, priceTo);
            } else if (categoryDtos != null && !categoryDtos.isEmpty()) {
                productEntities = productRepository.findByCategories(categoryEntities);
            } else if (priceFrom != null && priceTo != null) {
                productEntities = productRepository.findAllByPriceBetween(priceFrom, priceTo);
            } else if (priceFrom != null) {
                productEntities = productRepository.findByPriceGreaterThanEqual(priceFrom);
            } else if (priceTo != null) {
                productEntities = productRepository.findByPriceLessThanEqual(priceTo);
            } else {
                productEntities = productRepository.findAllIsNotDeleted();
            }
        }
        List<ProductDto> productDtos = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        return productDtos;
    }
    @Override
    public List<ProductDto> getAll() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductDto> productDtos = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
                .toList();
        return productDtos;
    }
    @Override
    public List<ProductDto> getAllNotDeleted(){
        List<ProductEntity> productEntities = productRepository.findAllIsNotDeleted();
        List<ProductDto> productDtos = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
                .toList();
        return productDtos;
    }
    @Override
    public List<ProductDto> getByName(String searchProduct) {
        if (searchProduct == null || searchProduct.isEmpty()) {
            List<ProductEntity> productEntities=productRepository.findAll();
            List<ProductDto> productDtos = productEntities.stream()
                    .map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
                    .toList();
            return productDtos;
        }
        List<ProductEntity> productEntities = productRepository.findByNameContainingIgnoreCase(searchProduct);
        List<ProductDto> productDtos = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
                .toList();
        return productDtos;
    }



}
