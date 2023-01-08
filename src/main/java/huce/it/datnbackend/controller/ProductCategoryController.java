package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.services.productcategory.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
//@RequestMapping("/productcate")
public class ProductCategoryController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    IProductCategoryService productCategoryService;

    @RequestMapping("/categories")
    public String showProductCategoryPage(Model model,
                                          HttpSession session, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductCategoryEntity> categories = productCategoryService.getAll();
        model.addAttribute("categories",categories);
        model.addAttribute("size",categories.size());
        model.addAttribute("title","Product Category");

        model.addAttribute("categoryNew", new ProductCategoryEntity());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/categories";
    }

    @GetMapping("/categories/{pageNo}")
    public String productCategoryPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<ProductCategoryEntity> categories = productCategoryService.pageCategories(pageNo);
        model.addAttribute("title", "Product Category");
        model.addAttribute("size", categories.getSize());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryNew", new ProductCategoryEntity());
        return "/admin/categories";
    }

    @GetMapping("/search-result-category/{pageNo}")
    public String searchCategories(@PathVariable("pageNo") int pageNo,
                               @RequestParam("keyword") String keyword,
                               Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<ProductCategoryEntity> categories = productCategoryService.searchCategories(pageNo, keyword);
        model.addAttribute("title", "Product Category");
        model.addAttribute("size", categories.getSize());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryNew", new ProductCategoryEntity());
        model.addAttribute("keyword", keyword);

        return "admin/result-categories";

    }

    @PostMapping("/add-category")
    public String addProductCategory(@ModelAttribute("categoryNew") ProductCategoryEntity category,
                                     RedirectAttributes attributes,
                                     Model model){
        StringBuilder string = new StringBuilder();
        try {
            category.setCreateDate(new Timestamp(System.currentTimeMillis()));
            category.setStatus(1);
            productCategoryService.insertObject(category);
            attributes.addFlashAttribute("message", string.append("Added successfully product category ID: " + category.getId()));
        }catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("message", string.append("Failed to add because duplicate name"));
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Error Server"));
        }
        return "redirect:/categories/0";
    }

    @RequestMapping(value = "/findById", method = {RequestMethod.GET, RequestMethod.PUT})
    @ResponseBody
    public ProductCategoryEntity findById(int id) {
        return productCategoryService.getObjectById(id);
    }

    @GetMapping("/update-category")
    public String updateProductCategory(ProductCategoryEntity category, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        if(category.getId() != 0){
            try {
                productCategoryService.updateObject(category);
                attributes.addFlashAttribute("message", string.append("Updated successfully product category ID: " + category.getId()));
            } catch (DataIntegrityViolationException e) {
                attributes.addFlashAttribute("message", string.append("Failed to update because duplicate name"));
            }catch (Exception e) {
                e.printStackTrace();
                attributes.addFlashAttribute("message", string.append("Error Server"));
            }
        }
        return "redirect:/categories/0";
    }

    @RequestMapping(value = "/delete-category", method = {RequestMethod.GET, RequestMethod.PUT})
    public String deleteProductCate(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            productCategoryService.deleteObject(id);
            attributes.addFlashAttribute("message", string.append("Deleted successfully product category ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/categories/0";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.GET, RequestMethod.PUT})
    public String enableProductCate(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            productCategoryService.enableObject(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully product category ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/categories/0";
    }

    //-------------------------------------------------------------------------------------------------------------------------
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
