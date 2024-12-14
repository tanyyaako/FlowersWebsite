package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
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

    @Query("select p from ProductEntity p where p.category = :category and p.deleted = false")
    List<ProductEntity> findAllByCategoryAndIsDeletedFalse(@Param("category") CategoryEntity category);

    @Query("select p from ProductEntity p where p.salePrice is not null")
    List<ProductEntity> findAllBySale();

    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories")
    List<ProductEntity> findByCategories(@Param("categories") List<CategoryEntity> categories);
    @Query("SELECT DISTINCT p FROM ProductEntity p WHERE p.category IN :categories AND p.price >= :priceFrom AND p.price <= :priceTo")
    List<ProductEntity> findByCategoriesInAndPriceBetween(@Param("categories") List<CategoryEntity> categories, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo);
    @Query(value=" select p from ProductEntity p where p.price between :priceFrom and :priceTo ")
    List<ProductEntity> findAllByPriceBetween(@Param("priceFrom") Double priceFrom,@Param("priceTo") Double priceTo);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom")
    List<ProductEntity> findByPriceGreaterThanEqual(@Param("priceFrom") Double priceFrom);

    @Query("SELECT p FROM ProductEntity p WHERE p.price <= :priceTo")
    List<ProductEntity> findByPriceLessThanEqual(@Param("priceTo") Double priceTo);
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories AND p.price >= :priceFrom AND p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findByCategoriesInAndPriceBetweenAndNameContainingIgnoreCase(@Param("categories") List<CategoryEntity> categories, @Param("priceFrom") Double priceFrom,
                                                                                     @Param("priceTo") Double priceTo, @Param("name") String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.category IN :categories AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findByCategoriesAndNameContainingIgnoreCase(@Param("categories") List<CategoryEntity> categories, @Param("name") String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom AND p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findAllByPriceBetweenAndNameContainingIgnoreCase(@Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo,
                                                                         @Param("name") String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.price >= :priceFrom AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findByPriceGreaterThanEqualAndNameContainingIgnoreCase(@Param("priceFrom") Double priceFrom, @Param("name") String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.price <= :priceTo AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductEntity> findByPriceLessThanEqualAndNameContainingIgnoreCase(@Param("priceTo") Double priceTo, @Param("name") String name);


}
