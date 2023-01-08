package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer> {
    @Query("Select a from OrderDetailEntity a")
    List<OrderDetailEntity> findAllActive();

    @Query("Select p from OrderDetailEntity p where p.orderShop.id = ?1 ")
    List<OrderDetailEntity> findAllByOrderId(int orderShop);
}
