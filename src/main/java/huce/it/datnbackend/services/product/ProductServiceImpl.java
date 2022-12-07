package huce.it.datnbackend.services.product;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductRepository repository;

    @Override
    public List<ProductEntity> getAll() {
        return repository.findAllActive();
    }

    @Override
    public ProductEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(ProductEntity productEntity) {
        repository.save(productEntity);
        return 0;
    }

    @Override
    public int updateObject(ProductEntity productEntity) {
        repository.save(productEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        ProductEntity productEntity = repository.findById(id).get();
        productEntity.setStatus(0);
        repository.save(productEntity);
        return 200;
    }
}
