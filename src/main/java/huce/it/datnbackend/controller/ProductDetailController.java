package huce.it.datnbackend.controller;


import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.services.product.IProductService;
import huce.it.datnbackend.services.productdetail.IProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/productdetail")
public class ProductDetailController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    IProductService productService;

    @Autowired
    IProductDetailService productDetailService;

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
        if(productDetail.getId() != 0){
            productDetail.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            productDetail.setStatus(1);
            productDetailService.updateObject(productDetail);
        }else{
            productDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));
            productDetail.setStatus(1);
            productDetailService.insertObject(productDetail);
        }
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
