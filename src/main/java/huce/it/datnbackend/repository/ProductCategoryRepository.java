package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Integer> {
    @Query("Select a from ProductCategoryEntity a where a.status = 1")
    List<ProductCategoryEntity> findAllActive();

    @Query("Select e from ProductCategoryEntity e where e.status = 1")
    Page<ProductCategoryEntity> findAllActive(Pageable pageable);
}
