package huce.it.datnbackend.services.productdetail;

import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.paging.Paging;
import huce.it.datnbackend.repository.ProductDetailRepository;
import huce.it.datnbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailServiceImpl implements IProductDetailService {

    @Autowired
    private ProductDetailRepository repository;

    @Override
    public List<ProductDetailEntity> getAll() {
        return repository.findAllActive();

    }

    @Override
    public ProductDetailEntity getObjectById(int id) {
        return repository.findById(id).get();

    }

    @Override
    public int insertObject(ProductDetailEntity productDetailEntity) {
        repository.save(productDetailEntity);
        return 0;
    }

    @Override
    public int updateObject(ProductDetailEntity productDetailEntity) {
        repository.save(productDetailEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        ProductDetailEntity productDetailEntity = repository.findById(id).get();
        productDetailEntity.setStatus(0);
        repository.save(productDetailEntity);
        return 200;
    }

    @Override
    public Paged<ProductDetailEntity> getPage(int pageNumber) {
        return null;
    }
}
