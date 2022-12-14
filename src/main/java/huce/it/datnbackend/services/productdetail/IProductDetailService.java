package huce.it.datnbackend.services.productdetail;

import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IProductDetailService extends IFunctionService<ProductDetailEntity> {
    @Override
    List<ProductDetailEntity> getAll();

    List<ProductDetailEntity> getObjectByProductId(int id);

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
