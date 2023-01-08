package huce.it.datnbackend.services.product;

import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.paging.Paging;
import huce.it.datnbackend.repository.ProductRepository;
import huce.it.datnbackend.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public List<ProductDto> findAll() {
        List<ProductEntity> products = repository.findAllProduct();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public List<ProductDto> findAllByBrand(int id) {
        List<ProductEntity> products = repository.findAllProduct();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public ProductEntity save(MultipartFile imageProduct, ProductDto productDto) {
        try{
            ProductEntity product = new ProductEntity();
            if (imageProduct == null){
                product.setImage(null);
            }else{
                if (imageUpload.uploadImage(imageProduct)){
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }

            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setBrand(productDto.getBrand());
            product.setName(productDto.getName());
            product.setQuantity(productDto.getQuantity());
            product.setHot(productDto.getHot());
            product.setDescription(productDto.getDescription());
            product.setCreateDate(productDto.getCreateDate());
            product.setUpdateDate(productDto.getUpdateDate());
            product.setStatus(productDto.getStatus());
            product.setPrice(productDto.getPrice());
            return repository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductEntity update(MultipartFile imageProduct, ProductDto productDto) {
        try{
            ProductEntity product = repository.getByProductId(productDto.getId());
            if(imageProduct.getSize() == 0 || imageProduct == null){
                product.setImage(product.getImage());
            }else{
                if(imageUpload.checkExisted(imageProduct) == false){
                    imageUpload.uploadImage(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setBrand(productDto.getBrand());
            product.setName(productDto.getName());
            product.setQuantity(productDto.getQuantity());
            product.setHot(productDto.getHot());
            product.setDescription(productDto.getDescription());
            product.setCreateDate(productDto.getCreateDate());
            product.setUpdateDate(productDto.getUpdateDate());
            product.setStatus(productDto.getStatus());
            product.setPrice(productDto.getPrice());
            return repository.save(product);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        ProductEntity  product = repository.getById(id);
        product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        product.setStatus(0);
        repository.save(product);
    }

    @Override
    public void enableById(int id) {
        ProductEntity  product = repository.getById(id);
        product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        product.setStatus(1);
        repository.save(product);
    }

    @Override
    public ProductDto getById(int id) {
        ProductEntity product = repository.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setCategory(product.getCategory());
        productDto.setBrand(product.getBrand());
        productDto.setName(product.getName());
        productDto.setImage(product.getImage());
        productDto.setQuantity(product.getQuantity());
        productDto.setHot(product.getHot());
        productDto.setDescription(product.getDescription());
        productDto.setCreateDate(product.getCreateDate());
        productDto.setUpdateDate(product.getUpdateDate());
        productDto.setStatus(product.getStatus());
        productDto.setPrice(product.getPrice());
        return productDto;
    }

    @Override
    public Page<ProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> products = transfer(repository.findAllProduct());
        Page<ProductDto> productPages =  toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> pageProductsBrand(int pageNo, int id) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> products = transfer(repository.findAllProductByBrand(id));
        Page<ProductDto> productPages =  toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(repository.searchProductsList(keyword));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    @Override
    public Page<ProductDto> searchProductsBrand(int pageNo, String keyword, int id) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(repository.searchProductsBrandList(keyword, id));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    private Page toPage(List<ProductDto> list, Pageable pageable){
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

    private List<ProductDto> transfer(List<ProductEntity> products){
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity product :  products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setCategory(product.getCategory());
            productDto.setBrand(product.getBrand());
            productDto.setName(product.getName());
            productDto.setImage(product.getImage());
            productDto.setQuantity(product.getQuantity());
            productDto.setHot(product.getHot());
            productDto.setDescription(product.getDescription());
            productDto.setCreateDate(product.getCreateDate());
            productDto.setUpdateDate(product.getUpdateDate());
            productDto.setStatus(product.getStatus());
            productDto.setPrice(product.getPrice());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    //Customer
    @Override
    public List<ProductEntity> getAllProducts() {
        return repository.getAllProducts();
    }

    @Override
    public List<ProductEntity> listViewProducts() {
        return repository.listViewProducts();
    }

    @Override
    public ProductEntity getProductById(int id) {
        return repository.getByProductId(id);
    }

    @Override
    public List<ProductEntity> getRelatedProducts(int categoryId) {
        return repository.getRelatedProducts(categoryId);
    }

    @Override
    public List<ProductEntity> getProductsInCategory(int categoryId) {
        return repository.getProductsInCategory(categoryId);
    }

    @Override
    public List<ProductEntity> getProductsInBrand(int brandId) {
        return repository.getProductsInBrand(brandId);
    }

    @Override
    public List<ProductEntity> filterHighPrice(int categoryId) {
        return repository.filterHighPrice();
    }

    @Override
    public List<ProductEntity> filterLowPrice(int categoryId) {
        return repository.filterLowPrice();
    }


    //-----------------------------------------------------------------------

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

    @Override
    public Paged<ProductEntity> getPage(int pageNumber) {
        Page<ProductEntity> postPage = repository.findAllActive(PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE));
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
    }

    @Override
    public Paged<ProductEntity> getPage(int pageNumber, BrandEntity brand) {
        Page<ProductEntity> postPage = repository.findAllProductBrand(brand, PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE));
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
    }

    @Override
    public int insertFile(MultipartFile[] multipartFiles, ProductEntity product) {
        try{
            Path currentPath = Paths.get("");
            Path absolutePath = currentPath.toAbsolutePath();
            String imagePath = absolutePath + "/src/main/webapp/static/upload/" + product.getId();
            Path path = Paths.get(imagePath);
            if(!java.nio.file.Files.exists(path)){
                java.nio.file.Files.createDirectories(path);
            }
            for(MultipartFile multipartFile : multipartFiles){
                path = Paths.get(imagePath + "/" + multipartFile.getOriginalFilename());
                byte[] bytes = multipartFile.getBytes();
                java.nio.file.Files.write(path,bytes);
            }
            return 200;
        }catch (Exception e){
            e.printStackTrace();
            return 400;
        }
    }
}
