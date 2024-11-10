package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends GeneralRepository{
    @Query(value="select p from ProductEntity p where p.category=:category")
    Set<ProductEntity> findByCategory(@Param("categoty") CategoryEntity category);

    @Query(value=" select p from ProductEntity p where p.price between :startPrice and :finishPrice ")
    Set<ProductEntity> findAllByPriceBetween(@Param("starPrice") Double startPrice,@Param("finichPrice") Double finishPrice);
}
