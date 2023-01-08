package huce.it.datnbackend.services.productdetail;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.dto.ProductDetailDto;
import huce.it.datnbackend.model.OrderDetailEntity;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
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
    public ProductDetailEntity add(ProductDetailDto productDetailDto) {
        ProductDetailEntity productDetail = new ProductDetailEntity();
        productDetail.setProduct(productDetailDto.getProduct());
        productDetail.setPrice(productDetailDto.getPrice());
        productDetail.setPromotionPrice(productDetailDto.getPromotionPrice());
        productDetail.setQuantity(productDetailDto.getQuantity());
        productDetail.setSize(productDetailDto.getSize());
        productDetail.setCreateDate(productDetailDto.getCreateDate());
        productDetail.setUpdateDate(productDetailDto.getUpdateDate());
        productDetail.setStatus(productDetailDto.getStatus());
        repository.save(productDetail);
        return productDetail;
    }

    @Override
    public ProductDetailEntity update(ProductDetailDto productDetailDto) {
        try{
            ProductDetailEntity productDetail = new ProductDetailEntity();
            productDetail.setId(productDetailDto.getId());
            productDetail.setProduct(productDetailDto.getProduct());
            productDetail.setPrice(productDetailDto.getPrice());
            productDetail.setPromotionPrice(productDetailDto.getPromotionPrice());
            productDetail.setQuantity(productDetailDto.getQuantity());
            productDetail.setSize(productDetailDto.getSize());
            productDetail.setCreateDate(productDetailDto.getCreateDate());
            productDetail.setUpdateDate(productDetailDto.getUpdateDate());
            productDetail.setStatus(productDetailDto.getStatus());
            repository.save(productDetail);
            return productDetail;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        ProductDetailEntity productDetail = repository.getById(id);
        productDetail.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        productDetail.setStatus(0);
        repository.save(productDetail);

    }

    @Override
    public void enableById(int id) {
        ProductDetailEntity productDetail = repository.getById(id);
        productDetail.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        productDetail.setStatus(1);
        repository.save(productDetail);
    }

    @Override
    public List<ProductDetailEntity> getObjectByProductId(int productId) {
        return repository.findAllByProductId(productId);
    }

    @Override
    public List<ProductDetailDto> getByProductId(int id) {
        List<ProductDetailEntity> productDetails = repository.findByProductId(id);
        List<ProductDetailDto> productDetailDtoList = transfer(productDetails);
        return productDetailDtoList;
    }
    private List<ProductDetailDto> transfer(List<ProductDetailEntity> productDetails){
        List<ProductDetailDto> productDetailDtoList = new ArrayList<>();
        for (ProductDetailEntity productDetail :  productDetails){
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setId(productDetail.getId());
            productDetailDto.setProduct(productDetail.getProduct());
            productDetailDto.setPrice(productDetail.getPrice());
            productDetailDto.setPromotionPrice(productDetail.getPromotionPrice());
            productDetailDto.setQuantity(productDetail.getQuantity());
            productDetailDto.setSize(productDetail.getSize());
            productDetailDto.setStatus(productDetail.getStatus());
            productDetailDto.setCreateDate(productDetail.getCreateDate());
            productDetailDto.setUpdateDate(productDetail.getUpdateDate());
            productDetailDtoList.add(productDetailDto);
        }
        return productDetailDtoList;
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
