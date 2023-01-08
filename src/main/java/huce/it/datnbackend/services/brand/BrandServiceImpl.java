package huce.it.datnbackend.services.brand;

import huce.it.datnbackend.dto.BrandDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class BrandServiceImpl implements IBrandService{

    @Autowired
    private BrandRepository repository;

    @Override
    public List<BrandEntity> getAll() {
        return repository.findAllSortID();
    }

    @Override
    public BrandEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(BrandEntity brandEntity) {
        repository.save(brandEntity);
        return 0;
    }

    @Override
    public int updateObject(BrandEntity brandEntity) {
        BrandEntity brand = repository.findById(brandEntity.getId()).get();
        brandEntity.setCreatedDate(brand.getCreatedDate());
        brandEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        brandEntity.setStatus(brand.getStatus());
        repository.save(brandEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        BrandEntity brandEntity = repository.findById(id).get();
        brandEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        brandEntity.setStatus(0);
        repository.save(brandEntity);
        return 200;
    }

    @Override
    public int enableObject(int id) {
        BrandEntity brandEntity = repository.findById(id).get();
        brandEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        brandEntity.setStatus(1);
        repository.save(brandEntity);
        return 200;
    }

    @Override
    public Paged<BrandEntity> getPage(int pageNumber) {
        return null;
    }

    @Override
    public Page<BrandEntity> pageBrands(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<BrandEntity> brands = repository.findAllSortID();
        Page<BrandEntity> brandPage =  toPage(brands, pageable);
        return brandPage;
    }

    @Override
    public Page<BrandEntity> searchBrands(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<BrandEntity> brandList = repository.searchBrandsList(keyword);
        Page<BrandEntity> brands = toPage(brandList, pageable);
        return brands;
    }

    @Override
    public List<BrandDto> getBrandAndProduct() {
        return repository.getBrandAndProduct();
    }

    private Page toPage(List<BrandEntity> list, Pageable pageable){
        if (pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }
}
