package huce.it.datnbackend.services.productdetail;

import huce.it.datnbackend.dto.ProductDetailDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductDetailService extends IFunctionService<ProductDetailEntity> {
    @Override
    List<ProductDetailEntity> getAll();
    ProductDetailEntity add(ProductDetailDto productDetailDto);
    ProductDetailEntity update(ProductDetailDto productDetailDto);
    void deleteById(int id);
    void enableById(int id);
    List<ProductDetailEntity> getObjectByProductId(int id);
    List<ProductDetailDto> getByProductId(int id);

    @Override
    ProductDetailEntity getObjectById(int id);

    @Override
    int insertObject(ProductDetailEntity productDetailEntity);

    @Override
    int updateObject(ProductDetailEntity productDetailEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<ProductDetailEntity> getPage(int pageNumber);
}
