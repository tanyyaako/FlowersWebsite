package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.DTO.CategoryDto;
import org.example.flowerswebsite.DTO.PageDto;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.CategoryRepository;
import org.example.flowerswebsite.Repositories.OrderContentRepository;
import org.example.flowerswebsite.Repositories.ProductRepository;
import org.example.flowerswebsite.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @CacheEvict(value = "catalogCache", allEntries = true)
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
    @CacheEvict(value = "catalogCache", allEntries = true)
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
    @CacheEvict(value = "catalogCache", allEntries = true)
    public void delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productEntity.setDeleted(true);
        productRepository.save(productEntity);
    }

    @Override
    @CacheEvict(value = "catalogCache", allEntries = true)
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
    @CacheEvict(value = "catalogCache", allEntries = true)
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
    public Page<ProductDto> getByCategoriesOrPrice(List<CategoryDto> categoryDtos, Double priceFrom,
                                                   Double priceTo,String name,int page,int size) {
        List<CategoryEntity> categoryEntities = null;
        Pageable pageable= PageRequest.of(page,size);
        if (categoryDtos != null && !categoryDtos.isEmpty()) {
            categoryEntities = categoryDtos.stream()
                    .map(categoryDto -> modelMapper.map(categoryDto, CategoryEntity.class))
                    .toList();
        }
        Page<ProductEntity> productEntities;
        if (name != null) {
            if (categoryDtos != null && !categoryDtos.isEmpty() && priceFrom != null && priceTo != null) {
                productEntities = productRepository.findByCategoriesInAndPriceBetweenAndNameContainingIgnoreCase(
                        categoryEntities, priceFrom, priceTo, name,pageable);
            } else if (categoryDtos != null && !categoryDtos.isEmpty()) {
                productEntities = productRepository.findByCategoriesAndNameContainingIgnoreCase(categoryEntities, name,pageable);
            } else if (priceFrom != null && priceTo != null) {
                productEntities = productRepository.findAllByPriceBetweenAndNameContainingIgnoreCase(priceFrom, priceTo, name,pageable);
            } else if (priceFrom != null) {
                productEntities = productRepository.findByPriceGreaterThanEqualAndNameContainingIgnoreCase(priceFrom, name,pageable);
            } else if (priceTo != null) {
                productEntities = productRepository.findByPriceLessThanEqualAndNameContainingIgnoreCase(priceTo, name,pageable);
            } else {
                productEntities = productRepository.findByNameContainingIgnoreCase(name,pageable);
            }
        } else {
            if (categoryDtos != null && !categoryDtos.isEmpty() && priceFrom != null && priceTo != null) {
                productEntities = productRepository.findByCategoriesInAndPriceBetween(categoryEntities, priceFrom, priceTo,pageable);
            } else if (categoryDtos != null && !categoryDtos.isEmpty()) {
                productEntities = productRepository.findByCategoriesPage(categoryEntities,pageable);
            } else if (priceFrom != null && priceTo != null) {
                productEntities = productRepository.findAllByPriceBetween(priceFrom, priceTo,pageable);
            } else if (priceFrom != null) {
                productEntities = productRepository.findByPriceGreaterThanEqual(priceFrom,pageable);
            } else if (priceTo != null) {
                productEntities = productRepository.findByPriceLessThanEqual(priceTo,pageable);
            } else {
                productEntities = productRepository.findAllIsNotDeletedPage(pageable);
            }
        }
        Page<ProductDto> productDtos = productEntities
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class));
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
//    @Override
//    public List<ProductDto> getByName(String searchProduct) {
//        if (searchProduct == null || searchProduct.isEmpty()) {
//            List<ProductEntity> productEntities=productRepository.findAll();
//            List<ProductDto> productDtos = productEntities.stream()
//                    .map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
//                    .toList();
//            return productDtos;
//        }
//        List<ProductEntity> productEntities = productRepository.findByNameContainingIgnoreCase(searchProduct);
//        List<ProductDto> productDtos = productEntities.stream()
//                .map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
//                .toList();
//        return productDtos;
//    }

    @Override
    @Cacheable(value = "catalogCache", key = "#categoryType")
    public PageDto<ProductDto> getPageProductsByCategories(List<Long> categoryIds,int page,int size,String categoryType){
        Pageable pageable= PageRequest.of(page,size);
        Page<ProductEntity> pageProductEntity = productRepository.getPageProductsInCategories(categoryIds,pageable);
        Page<ProductDto> pageProductDTO = pageProductEntity
                .map(productEntity -> modelMapper.map(productEntity,ProductDto.class));
        return new PageDto<>(pageProductDTO);

    }
}
