package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    @Query("Select a from BrandEntity a where a.status = 1")
    List<BrandEntity> findAllActive();
}
