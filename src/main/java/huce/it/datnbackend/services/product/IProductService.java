package huce.it.datnbackend.services.product;

import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService extends IFunctionService<ProductEntity> {

    //Admin
    List<ProductDto> findAll();

    List<ProductDto> findAllByBrand(int id);

    ProductEntity save(MultipartFile imageProduct, ProductDto productDto);
    ProductEntity update(MultipartFile imageProduct, ProductDto productDto);
    void deleteById(int id);
    void enableById(int id);
    ProductDto getById(int id);
    Page<ProductDto> pageProducts(int pageNo);

    Page<ProductDto> pageProductsBrand(int pageNo, int id);
    Page<ProductDto> searchProducts(int pageNo, String keyword);

    Page<ProductDto> searchProductsBrand(int pageNo, String keyword, int id);

    //Customer
    List<ProductEntity> getAllProducts();

    List<ProductEntity> listViewProducts();

    ProductEntity getProductById(int id);

    List<ProductEntity> getRelatedProducts(int categoryId);

    List<ProductEntity> getProductsInCategory(int categoryId);

    List<ProductEntity> getProductsInBrand(int brandId);

    List<ProductEntity> filterHighPrice(int categoryId);

    List<ProductEntity> filterLowPrice(int categoryId);



    //Shop
    @Override
    List<ProductEntity> getAll();

    @Override
    ProductEntity getObjectById(int id);

    @Override
    int insertObject(ProductEntity productEntity);

    @Override
    int updateObject(ProductEntity productEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<ProductEntity> getPage(int pageNumber);

    Paged<ProductEntity> getPage(int pageNumber, BrandEntity brand);

    int insertFile(MultipartFile[] multipartFiles, ProductEntity product);
}
