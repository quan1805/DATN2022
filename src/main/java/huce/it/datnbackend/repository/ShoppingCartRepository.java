package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import huce.it.datnbackend.model.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Integer> {
    @Query("Select p from ShoppingCartEntity p where p.customer.id = ?1 ")
    ShoppingCartEntity findByCustomer(int customerId);
}
