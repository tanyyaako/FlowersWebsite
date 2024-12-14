package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends GeneralRepository<UserEntity, Long> {
    @Query(value="select u from UserEntity u where u.username=:username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    Optional<UserEntity> findByEmail(String email);
}
