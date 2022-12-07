package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.services.brand.IBrandService;
import huce.it.datnbackend.services.product.IProductService;
import huce.it.datnbackend.services.productcategory.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductCategoryService productCategoryService;

    @Autowired
    private IBrandService brandService;

    @RequestMapping("/manager_product")
    public String showProductManagerPage(Model model,
                                       HttpSession session){
        model.addAttribute("products",productService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/product_list";
    }

    @RequestMapping("/manager_add_product")
    public String addProductManagerPage(Model model,
                                      HttpSession session){
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getAll());

//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/product_add";
    }

    @PostMapping("/save_product_action")
    public String addProduct(@ModelAttribute("product") ProductEntity product){
        if(product.getId() != 0){
            product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            product.setStatus(1);
            productService.updateObject(product);
        }else{
            product.setCreateDate(new Timestamp(System.currentTimeMillis()));
            product.setStatus(1);
            productService.insertObject(product);
        }
        return "redirect:/product/manager_product";
    }

    @RequestMapping("/update_product")
    public String showUpdateProductPage(@RequestParam("productId")int productId,
                                      Model model,
                                      HttpSession session){
        model.addAttribute("product",productService.getObjectById(productId));
        model.addAttribute("productcates",productCategoryService.getAll());
        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "brand/product_info";

    }

    @RequestMapping("/delete_product")
    public String deleteProduct(@RequestParam("productId")int productId) {
        if(validateDelete(productId)){
            productService.deleteObject(productId);
        }
        return "redirect:/product/manager_product";
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
