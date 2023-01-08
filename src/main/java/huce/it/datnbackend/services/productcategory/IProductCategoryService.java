package huce.it.datnbackend.services.productcategory;

import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.dto.ProductDetailDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.springframework.data.domain.Page;

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

    int enableObject(int id);

    @Override
    Paged<ProductCategoryEntity> getPage(int pageNumber);
    Page<ProductCategoryEntity> pageCategories(int pageNo);
    Page<ProductCategoryEntity> searchCategories(int pageNo, String keyword);

    //Customer
    List<ProductCategoryDto> getCategoryAndProduct();
}
