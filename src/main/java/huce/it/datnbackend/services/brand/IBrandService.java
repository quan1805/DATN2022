package huce.it.datnbackend.services.brand;

import huce.it.datnbackend.dto.BrandDto;
import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBrandService extends IFunctionService<BrandEntity> {
    @Override
    List<BrandEntity> getAll();

    @Override
    BrandEntity getObjectById(int id);

    @Override
    int insertObject(BrandEntity brandEntity);

    @Override
    int updateObject(BrandEntity brandEntity);

    @Override
    int deleteObject(int id);

    int enableObject(int id);

    @Override
    Paged<BrandEntity> getPage(int pageNumber);

    Page<BrandEntity> pageBrands(int pageNo);
    Page<BrandEntity> searchBrands(int pageNo, String keyword);

    List<BrandDto> getBrandAndProduct();
}
