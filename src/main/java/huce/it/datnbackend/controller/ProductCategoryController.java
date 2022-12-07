package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.services.productcategory.IProductCategoryService;
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
@RequestMapping("/productcate")
public class ProductCategoryController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    IProductCategoryService productCategoryService;

    @RequestMapping("/manager_productcate")
    public String showProductCateManagerPage(Model model,
                                       HttpSession session){
        model.addAttribute("productcates",productCategoryService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/productcate_list";
    }

    @RequestMapping("/manager_add_productcate")
    public String addProductCateManagerPage(Model model,
                                      HttpSession session){
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/productcate_add";
    }

    @PostMapping("/save_productcate_action")
    public String addProductCate(@ModelAttribute("productcate") ProductCategoryEntity productCategory){
        if(productCategory.getId() != 0){
            productCategory.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            productCategory.setStatus(1);
            productCategoryService.updateObject(productCategory);
        }else{
            productCategory.setCreateDate(new Timestamp(System.currentTimeMillis()));
            productCategory.setStatus(1);
            productCategoryService.insertObject(productCategory);
        }
        return "redirect:/productcate/manager_productcate";
    }

    @RequestMapping("/update_productcate")
    public String showUpdateProductCatePage(@RequestParam("productcateId")int productcateId,
                                      Model model,
                                      HttpSession session){
        model.addAttribute("productcate", productCategoryService.getObjectById(productcateId));
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "admin/productcate_info";

    }

    @RequestMapping("/delete_productcate")
    public String deleteProductCate(@RequestParam("productcateId")int productcateId) {
        if(validateDelete(productcateId)){
            productCategoryService.deleteObject(productcateId);
        }
        return "redirect:/productcate/manager_productcate";
    }

    private boolean validateDelete(int productcateID){
        if(productCategoryService.getAll().stream().map(ProductCategoryEntity::getId).collect(Collectors.toList()).contains(productcateID)){
            return true;
        }else{
            errors.add("Product Category không tồn tại");
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
