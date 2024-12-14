package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.OrderEntity;
import org.example.flowerswebsite.Entities.Enums.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends GeneralRepository<OrderEntity, Long> {
    @Query("select o from OrderEntity o where o.status =: status ")
    List<OrderEntity> findAllByStatus(@Param("status") OrderStatus Status);
}
