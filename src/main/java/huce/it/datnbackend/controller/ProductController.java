package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.BrandDto;
import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.dto.ProductDetailDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.*;
import huce.it.datnbackend.services.brand.IBrandService;
import huce.it.datnbackend.services.product.IProductService;
import huce.it.datnbackend.services.productcategory.IProductCategoryService;
import huce.it.datnbackend.services.productdetail.IProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductCategoryService productCategoryService;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private IBrandService brandService;

    //Customer
    @GetMapping("/lofa/products")
    public String cusProducts(Model model,
                              @RequestParam(value = "sort",required = false, defaultValue = "0") int sort){
        List<ProductEntity> products = productService.getAllProducts();
        switch (sort) {
            case 1: //High to Low
                products.sort(Comparator.comparing(ProductEntity::getPrice).reversed());
                model.addAttribute("sort", 1);
                break;
            case 2: //Low to High
                products.sort(Comparator.comparing(ProductEntity::getPrice));
                model.addAttribute("sort", 2);
                break;
            case 3: //Hot
                products.sort(Comparator.comparing(ProductEntity::getHot).reversed());
                model.addAttribute("sort", 3);
                break;
            default:
                model.addAttribute("sort", 0);
                break;
        }
        List<ProductEntity> listViewProducts = productService.listViewProducts();
        List<ProductCategoryDto> categories = productCategoryService.getCategoryAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("listViewProducts", listViewProducts);
        return "customer/shop";
    }

    @GetMapping("/lofa/brands")
    public String cusBrands(Model model,
                              @RequestParam(value = "sort",required = false, defaultValue = "0") int sort){
        List<ProductEntity> products = productService.getAllProducts();
        switch (sort) {
            case 1: //High to Low
                products.sort(Comparator.comparing(ProductEntity::getPrice).reversed());
                model.addAttribute("sort", 1);
                break;
            case 2: //Low to High
                products.sort(Comparator.comparing(ProductEntity::getPrice));
                model.addAttribute("sort", 2);
                break;
            case 3: //Hot
                products.sort(Comparator.comparing(ProductEntity::getHot).reversed());
                model.addAttribute("sort", 3);
                break;
            default:
                model.addAttribute("sort", 0);
                break;
        }
        List<ProductEntity> listViewProducts = productService.listViewProducts();
        List<BrandDto> brands = brandService.getBrandAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("brands", brands);
        model.addAttribute("listViewProducts", listViewProducts);
        return "customer/shop-brand";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") int id, Model model){
        ProductEntity product = productService.getProductById(id);
        List<ProductDetailEntity> productDetails = productDetailService.getObjectByProductId(id);
        List<ProductEntity> relatedProducts = productService.getRelatedProducts(product.getCategory().getId());
        model.addAttribute("product",  product);
        model.addAttribute("productDetails",  productDetails);
        model.addAttribute("cartItem",  new CartItemEntity());
        model.addAttribute("relatedProducts",  relatedProducts);

        return "customer/product-detail";
    }

    @GetMapping("/products-in-category/{cateId}")
    public String getProductsInCategory(@PathVariable("cateId") int id, Model model,
                                        @RequestParam(value = "sort",required = false, defaultValue = "0") int sort){
        List<ProductEntity> products = productService.getProductsInCategory(id);
        switch (sort) {
            case 1: //High to Low
                products.sort(Comparator.comparing(ProductEntity::getPrice).reversed());
                model.addAttribute("sort", 1);
                break;
            case 2: //Low to High
                products.sort(Comparator.comparing(ProductEntity::getPrice));
                model.addAttribute("sort", 2);
                break;
            case 3: //Hot
                products.sort(Comparator.comparing(ProductEntity::getHot).reversed());
                model.addAttribute("sort", 3);
                break;
            default:
                model.addAttribute("sort", 0);
                break;
        }
        ProductCategoryEntity category = productCategoryService.getObjectById(id);
        List<ProductCategoryDto> categories = productCategoryService.getCategoryAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);
        return "customer/products-in-category";
    }

    @GetMapping("/products-in-brand/{brandId}")
    public String getProductsInBrand(@PathVariable("brandId") int id, Model model,
                                        @RequestParam(value = "sort",required = false, defaultValue = "0") int sort){
        List<ProductEntity> products = productService.getProductsInBrand(id);
        switch (sort) {
            case 1: //High to Low
                products.sort(Comparator.comparing(ProductEntity::getPrice).reversed());
                model.addAttribute("sort", 1);
                break;
            case 2: //Low to High
                products.sort(Comparator.comparing(ProductEntity::getPrice));
                model.addAttribute("sort", 2);
                break;
            case 3: //Hot
                products.sort(Comparator.comparing(ProductEntity::getHot).reversed());
                model.addAttribute("sort", 3);
                break;
            default:
                model.addAttribute("sort", 0);
                break;
        }
        BrandEntity brand = brandService.getObjectById(id);
        List<BrandDto> brands = brandService.getBrandAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("brands", brands);
        model.addAttribute("brand", brand);
        return "customer/products-in-brand";
    }


    //Admin
    @GetMapping("/products")
    public String products(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        model.addAttribute("title", "Product");
        return "admin/products";
    }

    @GetMapping("/products/{pageNo}")
    public String productPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.pageProducts(pageNo);
        model.addAttribute("title", "Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "admin/result-products";

    }

    @GetMapping("/add-product")
    public String addProductForm(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("product", new ProductDto());
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getAll());
        return "admin/add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productDto.setStatus(1);
            productDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
            ProductEntity product = productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("message",
                    string.append("Added successfully product ID: " + product.getId()));
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message",
                    string.append("Failed"));
        }
        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") int id,
                                    Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Product");
        ProductDto productDto = productService.getById(id);
        List<ProductDetailDto> productDetails = productDetailService.getByProductId(id);
        model.addAttribute("product", productDto);
        model.addAttribute("productDetails", productDetails);
        model.addAttribute("productDetailNew", new ProductDetailDto());
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getAll());

        return "admin/update-product";
    }

    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") int id,
                                @ModelAttribute("product") ProductDto product,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            product.setStatus(1);
            product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            ProductEntity productEntity = productService.update(imageProduct, product);
            attributes.addFlashAttribute("message", string.append("Update successfully product ID: " + productEntity.getId()));
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Update failed"));

        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(@PathVariable("id") int id, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productService.enableById(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully product ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduct(@PathVariable("id") int id, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productService.deleteById(id);
            attributes.addFlashAttribute("message", string.append("Delete successfully product ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/products/0";
    }






    //Shop
    @GetMapping("/brand-products/{pageNo}")
    public String brandProductPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        Page<ProductDto> products = productService.pageProductsBrand(pageNo, brand.getId());
        model.addAttribute("title", "Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "brand/products";
    }

    @GetMapping("/brand-search-result/{pageNo}")
    public String brandSearchProducts(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        Page<ProductDto> products = productService.searchProductsBrand(pageNo, keyword, brand.getId());
        model.addAttribute("title", "Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "brand/result-products";

    }

    @GetMapping("/brand-add-product")
    public String brandAddProductForm(Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        model.addAttribute("product", new ProductDto());
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getObjectById(brand.getId()));
        return "brand/add-product";
    }

    @PostMapping("/brand-save-product")
    public String brandSaveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes attributes, HttpSession session){
        StringBuilder string = new StringBuilder();
        try{
            productDto.setStatus(1);
            productDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
            ProductEntity product = productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("message",
                    string.append("Added successfully product ID: " + product.getId()));
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message",
                    string.append("Failed"));
        }
        return "redirect:/brand-products/0";
    }
    @GetMapping("/brand-update-product/{id}")
    public String brandUpdateProductForm(@PathVariable("id") int id,
                                    Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        model.addAttribute("title", "Product");
        ProductDto productDto = productService.getById(id);
        List<ProductDetailDto> productDetails = productDetailService.getByProductId(id);
        model.addAttribute("product", productDto);
        model.addAttribute("productDetails", productDetails);
        model.addAttribute("productDetailNew", new ProductDetailDto());
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getObjectById(brand.getId()));

        return "brand/update-product";
    }

    @PostMapping("/brand-update-product/{id}")
    public String processBrandUpdate(@PathVariable("id") int id,
                                @ModelAttribute("product") ProductDto product,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            product.setStatus(1);
            product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            ProductEntity productEntity = productService.update(imageProduct, product);
            attributes.addFlashAttribute("message", string.append("Update successfully product ID: " + productEntity.getId()));
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Update failed"));

        }
        return "redirect:/brand-products/0";
    }

    @RequestMapping(value = "/brand-enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String brandEnabledProduct(@PathVariable("id") int id, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productService.enableById(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully product ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/brand-products/0";
    }

    @RequestMapping(value = "/brand-delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String brandDeleteProduct(@PathVariable("id") int id, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productService.deleteById(id);
            attributes.addFlashAttribute("message", string.append("Delete successfully product ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/brand-products/0";
    }



























    //Shop
    @RequestMapping("/manage_product")
    public String showProductManagerPage(@RequestParam(value = "pageNumber",required = false, defaultValue = "1")  int pageNumber,
                                        Model model,
                                       HttpSession session){

        if (session.getAttribute("user") instanceof BrandEntity) {
            BrandEntity brand = (BrandEntity) session.getAttribute("user");
            model.addAttribute("products",productService.getPage(pageNumber, brand));
        }
        else {
            model.addAttribute("orders",productService.getPage(pageNumber));
        }
//        BrandEntity brand = (BrandEntity) session.getAttribute("user");
//        model.addAttribute("products",productService.getPage(pageNumber, brand));
        model.addAttribute("sum", productService.getAll().size());
        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/product_list";
    }

    @RequestMapping("/manage_add_product")
    public String addProductManagerPage(Model model,
                                      HttpSession session){
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getAll());

//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/product_add";
    }

    @PostMapping("/save_product_action")
    public String addProduct(@ModelAttribute("product") ProductEntity product,
                             @RequestParam(value = "images", required = false) MultipartFile[] multipartFiles){
        if(product.getId() != 0){
            product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            product.setStatus(1);
            if(multipartFiles != null){
                StringBuilder fileName = new StringBuilder();
                for(MultipartFile multipartFile : multipartFiles){
                    fileName.append(multipartFile.getOriginalFilename());
                    fileName.append(";");
                }
                product.setImage(fileName.toString());
                productService.updateObject(product);
                productService.insertFile(multipartFiles, product);
            }else{
                productService.updateObject(product);
            }
        }else{
            product.setCreateDate(new Timestamp(System.currentTimeMillis()));
            product.setStatus(1);
            if(multipartFiles != null){
                StringBuilder fileName = new StringBuilder();
                for(MultipartFile multipartFile : multipartFiles){
                    fileName.append(multipartFile.getOriginalFilename());
                    fileName.append(";");
                }
                product.setImage(fileName.toString());
                productService.insertObject(product);
                productService.insertFile(multipartFiles, product);
            }else{
                productService.insertObject(product);

            }
        }
        return "redirect:/manage_product";
    }

    @RequestMapping("/update_product")
    public String showUpdateProductPage(@RequestParam(value = "productId", required = false) int productId,
                                      Model model,
                                      HttpSession session){
        ProductEntity product = productService.getObjectById(productId);
        model.addAttribute("product",product);
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("productdetails", productDetailService.getObjectByProductId(productId));
        model.addAttribute("brands",brandService.getAll());
        if(product.getImage() != null){
            String[] images = product.getImage().split(";");
            model.addAttribute("images",images);
        }
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/product_info";

    }

    @RequestMapping("/delete_product")
    public String deleteProduct(@RequestParam("productId")int productId) {
        if(validateDelete(productId)){
            productService.deleteObject(productId);
        }
        return "redirect:/manage_product";
    }

    private boolean validateDelete(int productId){
        if(productService.getAll().stream().map(ProductEntity::getId).collect(Collectors.toList()).contains(productId)){
            return true;
        }else{
            errors.add("Product không tồn tại");
            return false;
        }
    }

    private void sentError(Model model){
        if(errors.size() > 0){
            StringBuilder string = new StringBuilder();
            for(String error : errors){
                string.append(error);
                string.append('\n');
            }
            model.addAttribute("error",string);
            errors.clear();
        }
    }
}
