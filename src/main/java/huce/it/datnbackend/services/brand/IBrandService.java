package huce.it.datnbackend.services.brand;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

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

    @Override
    Paged<BrandEntity> getPage(int pageNumber);
}
