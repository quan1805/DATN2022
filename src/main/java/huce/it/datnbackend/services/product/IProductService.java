package huce.it.datnbackend.services.product;

import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IProductService extends IFunctionService<ProductEntity> {
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
}
