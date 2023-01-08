package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShopRepository extends JpaRepository<OrderShopEntity, Integer> {
    @Query("Select p from OrderShopEntity p where p.order.id = ?1 ")
    List<OrderShopEntity> findAllByOrderId(int orderId);

    @Query("Select p from OrderShopEntity p where p.brand.id = ?1 ")
    List<OrderShopEntity> findAllByBrandId(int id);

    @Query("select p from OrderShopEntity p where p.id = ?1 or p.status =?1 and p.brand.id = ?2")
    List<OrderShopEntity> searchOrdersBrandList(int keyword, int id);
}
