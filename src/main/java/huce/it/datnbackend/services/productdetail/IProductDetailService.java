package huce.it.datnbackend.services.productdetail;

import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IProductDetailService extends IFunctionService<ProductDetailEntity> {
    @Override
    List<ProductDetailEntity> getAll();

    @Override
    ProductDetailEntity getObjectById(int id);

    @Override
    int insertObject(ProductDetailEntity productDetailEntity);

    @Override
    int updateObject(ProductDetailEntity productDetailEntity);

    @Override
    int deleteObject(int id);
}
