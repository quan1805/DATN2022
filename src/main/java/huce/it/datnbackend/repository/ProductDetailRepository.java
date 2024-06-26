package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Integer> {
    @Query("Select a from ProductDetailEntity a where a.status = 1")
    List<ProductDetailEntity> findAllActive();

}
