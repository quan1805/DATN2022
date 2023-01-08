package huce.it.datnbackend.controller;


import huce.it.datnbackend.dto.ProductDetailDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.services.product.IProductService;
import huce.it.datnbackend.services.productdetail.IProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/productdetail")
public class ProductDetailController {
    private List<String> errors = new ArrayList<>();

    private int quantity = 0;

    @Autowired
    IProductService productService;

    @Autowired
    IProductDetailService productDetailService;

    @PostMapping("/add-product-detail/{productId}")
    public String addProductDetail(@PathVariable("productId") int productId,
                                   @ModelAttribute("productDetailNew") ProductDetailDto productDetailDto,
                                   RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productDetailDto.setStatus(1);
            productDetailDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
            productDetailDto.setProduct(productService.getObjectById(productId));
            ProductDetailEntity productDetail = productDetailService.add(productDetailDto);
            attributes.addFlashAttribute("message",
                    string.append("Added successfully product detail ID: " + productDetail.getId()));
            return "redirect:/update-product/" + productId;
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message",
                    string.append("Failed"));
            return "redirect:/update-product/" + productId;
        }

    }

    @RequestMapping(value = "/findByIdProductDetail", method = {RequestMethod.GET, RequestMethod.PUT})
    @ResponseBody
    public ProductDetailEntity findByIdProductDetail(int id) {
        return productDetailService.getObjectById(id);
    }

    @GetMapping("/update-product-detail/{productId}")
    public String updateProductCategory(@PathVariable("productId") int productId,
                                        ProductDetailDto productDetailDto, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        if(productDetailDto.getId() != 0){
            try {
                ProductDetailEntity productUpdate = productDetailService.getObjectById(productDetailDto.getId());
                productDetailDto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                productDetailDto.setCreateDate(productUpdate.getCreateDate());
                productDetailDto.setProduct(productService.getObjectById(productId));
                productDetailDto.setStatus(productUpdate.getStatus());

                productDetailService.update(productDetailDto);
                attributes.addFlashAttribute("message", string.append("Updated successfully product detail ID: " + productDetailDto.getId()));
            } catch (DataIntegrityViolationException e) {
                attributes.addFlashAttribute("message", string.append("Failed to update because duplicate name"));
            }catch (Exception e) {
                e.printStackTrace();
                attributes.addFlashAttribute("message", string.append("Error Server"));
            }
        }
        return "redirect:/update-product/" + productId;
    }

    @RequestMapping(value = "/delete-product-detail/{id}/{productId}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProductDetail(@PathVariable("id") int id,
                                @PathVariable("productId") int productId,
                                RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productDetailService.deleteById(id);
            attributes.addFlashAttribute("message", string.append("Delete successfully product detail ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/update-product/" + productId;
    }

    @RequestMapping(value = "/enable-product-detail/{id}/{productId}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(@PathVariable("id") int id,
                                 @PathVariable("productId") int productId,
                                 RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        try{
            productDetailService.enableById(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully product detail ID: " + id));

        }catch (Exception e){
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/update-product/" + productId;

    }


    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping("/manage_productdetail")
    public String showProductDetailManagerPage(Model model,
                                         HttpSession session){
        model.addAttribute("productdetails",productDetailService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/productdetail_list_t";
    }

    @RequestMapping("/manage_add_productdetail")
    public String addProductDetailManagerPage(@RequestParam("productId")int productId,
                                              Model model,
                                              HttpSession session){
//        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("product", productService.getObjectById(productId));
        sentError(model);
        return "/brand/productdetail_add";
    }

    @PostMapping("/save_productdetail_action")
    public String addProductDetail(@ModelAttribute("productdetail") ProductDetailEntity productDetail){
        quantity = 0;
        if(productDetail.getId() != 0){
            //update product detail
            productDetail.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            productDetail.setStatus(1);
            productDetailService.updateObject(productDetail);

        }else{
            productDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));
            productDetail.setStatus(1);
            productDetailService.insertObject(productDetail);
        }
        //update product quantity
        productDetailService.getObjectByProductId(productDetail.getProduct().getId()).stream().forEach(productDetailEntity -> {
            quantity += productDetailEntity.getQuantity();
        });
        ProductEntity product = productService.getObjectById(productDetail.getProduct().getId());
        product.setQuantity(quantity);
        product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        productService.updateObject(product);
        return "redirect:/update_product?productId=" + productDetail.getProduct().getId();
    }

    @RequestMapping("/update_productdetail")
    public String showUpdateProductDetailPage(@RequestParam("productDetailId")int productDetailId,
                                        Model model,
                                        HttpSession session){
        model.addAttribute("productdetail",productDetailService.getObjectById(productDetailId));
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/productdetail_info";

    }

    @RequestMapping("/delete_productdetail")
    public String deleteProductDetail(@RequestParam("productDetailId")int productId) {
        if(validateDelete(productId)){
            productDetailService.deleteObject(productId);
        }
        return "redirect:/manage_productdetail";
    }

    private boolean validateDelete(int productDetailId){
        if(productDetailService.getAll().stream().map(ProductDetailEntity::getId).collect(Collectors.toList()).contains(productDetailId)){
            return true;
        }else{
            errors.add("Product Detail không tồn tại");
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
