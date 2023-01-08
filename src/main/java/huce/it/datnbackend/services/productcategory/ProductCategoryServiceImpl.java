package huce.it.datnbackend.services.productcategory;

import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.ProductCategoryRepository;
import huce.it.datnbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public List<ProductCategoryEntity> getAll() {
        return repository.findAllCate();
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
        ProductCategoryEntity category = repository.findById(productCategoryEntity.getId()).get();
        category.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        category.setParentID(productCategoryEntity.getParentID());
        category.setName(productCategoryEntity.getName());
        repository.save(category);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        ProductCategoryEntity productCategoryEntity = repository.findById(id).get();
        productCategoryEntity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        productCategoryEntity.setStatus(0);
        repository.save(productCategoryEntity);
        return 200;
    }

    @Override
    public int enableObject(int id) {
        ProductCategoryEntity productCategoryEntity = repository.findById(id).get();
        productCategoryEntity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        productCategoryEntity.setStatus(1);
        repository.save(productCategoryEntity);
        return 200;
    }

    @Override
    public Paged<ProductCategoryEntity> getPage(int pageNumber) {
        return null;
    }

    @Override
    public Page<ProductCategoryEntity> pageCategories(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductCategoryEntity> categories = repository.findAllCate();
        Page<ProductCategoryEntity> categoryPage = toPage(categories, pageable);
        return categoryPage;
    }

    @Override
    public Page<ProductCategoryEntity> searchCategories(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductCategoryEntity> categoriesList = repository.searchCategoriesList(keyword);
        Page<ProductCategoryEntity> categories = toPage(categoriesList, pageable);
        return categories;
    }


    private Page toPage(List<ProductCategoryEntity> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    //Customer
    @Override
    public List<ProductCategoryDto> getCategoryAndProduct() {
        return repository.getCategoryAndProduct();
    }
}
