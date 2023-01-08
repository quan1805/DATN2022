package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Query("Select e from OrderEntity e")
    Page<OrderEntity> findAllActive(Pageable pageable);

    @Query("Select e from OrderEntity e order by  e.status asc, e.id asc")
    List<OrderEntity> findAllOrder();

    @Query("select p from OrderEntity p where p.id = ?1 or p.status =?1")
    List<OrderEntity> searchOrdersList(int keyword);

    @Query("Select p from OrderEntity p where p.status = :#{#status} ")
    Page<OrderEntity> findAllOrderByStatus(int status, Pageable pageable);
    @Query("Select p from OrderEntity p where p.customer.id = ?1 ")
    List<OrderEntity> findByCustomer(int customerId);


//    @Query("Select p from OrderEntity p where p.brand = ?1 ")
//    Page<OrderEntity> findAllOrderByBrand( Pageable pageable, BrandEntity brand);
//
//    @Query("Select p from OrderEntity p where p.status = ?1 and p.brand = ?2 ")
//    Page<OrderEntity> findAllOrderByStatusBrand(int status, Pageable pageable, BrandEntity brand);
}
