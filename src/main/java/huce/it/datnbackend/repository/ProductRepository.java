package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query("Select a from ProductEntity a where a.status = 1")
    List<ProductEntity> findAllActive();

    @Query("Select e from ProductEntity e where e.status = 1")
    Page<ProductEntity> findAllActive(Pageable pageable);
}
