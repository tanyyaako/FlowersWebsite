package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.OrderContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderContentRepository extends GeneralRepository<OrderContent,Long> {

    @Query(value="SELECT o.product, SUM(o.quantity) AS totalSold FROM OrderContent o GROUP BY o.product ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();


}
