package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Entities.StorageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StorageRepository extends GeneralRepository<StorageEntity, Long> {
    @Query(value="select s from StorageEntity s where s.adress=:adress")
    List<StorageEntity> findByAdress(@Param("adress") String adress);
}
