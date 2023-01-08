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
    @Query("Select a from ProductEntity a order by a.status desc, a.id asc ")
    List<ProductEntity> findAllProduct();

    @Query("Select a from ProductEntity a where a.brand.id = ?1 order by a.status desc, a.id asc ")
    List<ProductEntity> findAllProductByBrand(int id);

    @Query("Select e from ProductEntity e where e.status = 1")
    Page<ProductEntity> findAllActive(Pageable pageable);
    @Query("Select e from ProductEntity e where e.brand = ?1")
    Page<ProductEntity> findAllProductBrand(BrandEntity brand, Pageable pageable);

    //Admin
    @Query("Select p from ProductEntity p")
    Page<ProductEntity> pageProduct(Pageable pageable);

    @Query("select p from ProductEntity p where p.description like %?1% or p.name like %?1%")
    Page<ProductEntity> searchProducts(String keyword, Pageable pageable);

    @Query("select p from ProductEntity p where p.description like %?1% or p.name like %?1%")
    List<ProductEntity> searchProductsList(String keyword);

    @Query("select p from ProductEntity p where p.name like %?1% and p.brand.id = ?2")
    List<ProductEntity> searchProductsBrandList(String keyword, int id);

    @Query("Select p from ProductEntity p where p.id = ?1")
    ProductEntity getByProductId(int id);

    //Customer
    @Query("select p from ProductEntity p where p.status = 1 ")
    List<ProductEntity> getAllProducts();

    @Query(value = "select * from product p where p.status = 1 order by RANDOM() limit 8", nativeQuery = true)
    List<ProductEntity> listViewProducts();

    @Query(value = "select p from ProductEntity p inner join ProductCategoryEntity c on c.id = p.category.id where p.status = 1 and p.category.id = ?1 ")
    List<ProductEntity> getRelatedProducts(int categoryId);

    @Query(value = "select p from ProductEntity p inner join ProductCategoryEntity c on c.id = p.category.id where p.status = 1 and p.category.id = ?1 ")
    List<ProductEntity> getProductsInCategory(int categoryId);

    @Query(value = "select p from ProductEntity p inner join BrandEntity c on c.id = p.brand.id where p.status = 1 and p.brand.id = ?1 ")
    List<ProductEntity> getProductsInBrand(int brandId);

    @Query("select p from ProductEntity p where p.status = 1 order by p.price desc ")
    List<ProductEntity> filterHighPrice();

    @Query("select p from ProductEntity p where p.status = 1 order by p.price asc ")
    List<ProductEntity> filterLowPrice();


}

