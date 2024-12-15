package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends GeneralRepository<ProductEntity, Long> {
    @Query(value="select p from ProductEntity p where p.category=:category")
    Set<ProductEntity> findByCategory(@Param("category") CategoryEntity category);

    @Query(value = "select p from ProductEntity p where p.deleted=false")
    List<ProductEntity> findAllIsNotDeleted();
    @Query(value = "select p from ProductEntity p where p.deleted=false")
    Page<ProductEntity> findAllIsNotDeletedPage(Pageable pageable);

    @Query("select p from ProductEntity p where p.category = :category and p.deleted = false")
    List<ProductEntity> findAllByCategoryAndIsDeletedFalse(@Param("category") CategoryEntity category);

    @Query("select p from ProductEntity p where p.salePrice is not null and p.deleted = false")
    List<ProductEntity> findAllBySale();
    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories and p.deleted = false")
    List<ProductEntity> findByCategories(@Param("categories") List<CategoryEntity> categories);

    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories and p.deleted = false")
    Page<ProductEntity> findByCategoriesPage(@Param("categories") List<CategoryEntity> categories,Pageable pageable);
    @Query("SELECT DISTINCT p FROM ProductEntity p WHERE p.category IN :categories AND p.price >= :priceFrom AND p.price <= :priceTo and p.deleted = false")
    Page<ProductEntity> findByCategoriesInAndPriceBetween(@Param("categories") List<CategoryEntity> categories, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo,Pageable pageable);
    @Query(value=" select p from ProductEntity p where p.price between :priceFrom and :priceTo  and p.deleted = false")
    Page<ProductEntity> findAllByPriceBetween(@Param("priceFrom") Double priceFrom,@Param("priceTo") Double priceTo,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom and p.deleted = false")
    Page<ProductEntity> findByPriceGreaterThanEqual(@Param("priceFrom") Double priceFrom,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price <= :priceTo and p.deleted = false")
    Page<ProductEntity> findByPriceLessThanEqual(@Param("priceTo") Double priceTo,Pageable pageable);
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findByNameContainingIgnoreCase(@Param("name") String name,Pageable pageable);
    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories AND p.price >= :priceFrom AND p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findByCategoriesInAndPriceBetweenAndNameContainingIgnoreCase(@Param("categories") List<CategoryEntity> categories, @Param("priceFrom") Double priceFrom,
                                                                                     @Param("priceTo") Double priceTo, @Param("name") String name,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findByCategoriesAndNameContainingIgnoreCase(@Param("categories") List<CategoryEntity> categories, @Param("name") String name,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom AND p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findAllByPriceBetweenAndNameContainingIgnoreCase(@Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo,
                                                                         @Param("name") String name,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findByPriceGreaterThanEqualAndNameContainingIgnoreCase(@Param("priceFrom") Double priceFrom, @Param("name") String name,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) and p.deleted = false")
    Page<ProductEntity> findByPriceLessThanEqualAndNameContainingIgnoreCase(@Param("priceTo") Double priceTo, @Param("name") String name,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.deleted=false AND p.category.id IN :categoryIds")
    Page<ProductEntity> getPageProductsInCategories(@Param("categoryIds") List<Long> categoryIds,Pageable pageable);

}
