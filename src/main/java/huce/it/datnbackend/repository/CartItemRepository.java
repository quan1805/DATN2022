package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
}
