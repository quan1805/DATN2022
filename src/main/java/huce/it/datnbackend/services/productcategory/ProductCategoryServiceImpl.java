package huce.it.datnbackend.services.productcategory;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.ProductCategoryRepository;
import huce.it.datnbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService{

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public List<ProductCategoryEntity> getAll() {
        return repository.findAllActive();
    }

    @Override
    public ProductCategoryEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(ProductCategoryEntity productCategoryEntity) {
        repository.save(productCategoryEntity);
        return 0;
    }

    @Override
    public int updateObject(ProductCategoryEntity productCategoryEntity) {
        repository.save(productCategoryEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        ProductCategoryEntity productCategoryEntity = repository.findById(id).get();
        productCategoryEntity.setStatus(0);
        repository.save(productCategoryEntity);
        return 200;
    }

    @Override
    public Paged<ProductCategoryEntity> getPage(int pageNumber) {
        return null;
    }
}
