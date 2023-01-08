package huce.it.datnbackend.repository;

import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Integer> {
    @Query("Select a from ProductCategoryEntity a order by a.status desc, a.id asc")
    List<ProductCategoryEntity> findAllCate();
    @Query("Select a from ProductCategoryEntity a where a.status = 1")
    List<ProductCategoryEntity> findAllActive();

    @Query("Select e from ProductCategoryEntity e where e.status = 1")
    Page<ProductCategoryEntity> findAllActive(Pageable pageable);

    @Query("select p from ProductCategoryEntity p where p.name like %?1% order by p.status desc , p.id asc")
    List<ProductCategoryEntity> searchCategoriesList(String keyword);

    //Customer
    @Query("select new huce.it.datnbackend.dto.ProductCategoryDto(c.id, c.name, c.parentID, count(p.category.id))" +
            "from ProductCategoryEntity c inner join ProductEntity p on p.category.id = c.id where c.status = 1 group by c.id")
    List<ProductCategoryDto> getCategoryAndProduct();
}
