package huce.it.datnbackend.services.productcategory;

import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IProductCategoryService extends IFunctionService<ProductCategoryEntity> {
    @Override
    List<ProductCategoryEntity> getAll();

    @Override
    ProductCategoryEntity getObjectById(int id);

    @Override
    int insertObject(ProductCategoryEntity productCategoryEntity);

    @Override
    int updateObject(ProductCategoryEntity productCategoryEntity);

    @Override
    int deleteObject(int id);
}
