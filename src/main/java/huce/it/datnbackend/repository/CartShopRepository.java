package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.CartShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartShopRepository extends JpaRepository<CartShopEntity, Integer> {

}
