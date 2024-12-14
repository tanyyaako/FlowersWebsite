package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.CategoryType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends GeneralRepository<CategoryEntity, Long> {
    @Query("select c from CategoryEntity c join c.productEntities p where p.deleted = false ")
    List<CategoryEntity> findAllNotDeleted();
    @Query("SELECT c FROM CategoryEntity c WHERE c.categoryType = :categoryType")
    List<CategoryEntity> findByCategoryType(@Param("categoryType") CategoryType categoryType);
}
