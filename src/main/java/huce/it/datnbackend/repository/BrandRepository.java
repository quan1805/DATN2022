package huce.it.datnbackend.repository;

import huce.it.datnbackend.dto.BrandDto;
import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    @Query("Select a from BrandEntity a where a.status = 1")
    List<BrandEntity> findAllActive();

    @Query("Select a from BrandEntity a order by a.status desc, a.id asc ")
    List<BrandEntity> findAllSortID();

    @Query("select p from BrandEntity p where p.name like %?1% or p.address like %?1% order by p.status desc , p.id asc")
    List<BrandEntity> searchBrandsList(String keyword);

    @Query("select new huce.it.datnbackend.dto.BrandDto(c.id, c.name, count(p.brand.id))" +
            "from BrandEntity c inner join ProductEntity p on p.brand.id = c.id where c.status = 1 group by c.id")
    List<BrandDto> getBrandAndProduct();
}
