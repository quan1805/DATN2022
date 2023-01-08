package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.services.brand.IBrandService;
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
public class BrandController {

    private List<String> errors = new ArrayList<>();

    @Autowired
    IBrandService brandService;
    @RequestMapping("/brands")
    public String showBrandPage(Model model,
                                HttpSession session, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        List<BrandEntity> brands = brandService.getAll();
        model.addAttribute("brands",brands);
        model.addAttribute("size",brands.size());
        model.addAttribute("title","Brand");
        model.addAttribute("brandNew", new BrandEntity());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/brands";
    }

    @GetMapping("/brands/{pageNo}")
    public String brandPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<BrandEntity> brands = brandService.pageBrands(pageNo);
        model.addAttribute("title", "Brand");
        model.addAttribute("size", brands.getSize());
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("brands", brands);
        model.addAttribute("brandNew", new BrandEntity());
        return "admin/brands";
    }

    @GetMapping("/search-result-brand/{pageNo}")
    public String searchBrands(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<BrandEntity> brands = brandService.searchBrands(pageNo, keyword);
        model.addAttribute("title", "Brand");
        model.addAttribute("size", brands.getSize());
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("brands", brands);
        model.addAttribute("brandNew", new BrandEntity());
        model.addAttribute("keyword", keyword);

        return "admin/result-brands";

    }

    @PostMapping("/add-brand")
    public String addProductCategory(@ModelAttribute("brandNew") BrandEntity brand,
                                     RedirectAttributes attributes,
                                     Model model){
        StringBuilder string = new StringBuilder();
        try {
            brand.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            brand.setStatus(1);
            brandService.insertObject(brand);
            attributes.addFlashAttribute("message", string.append("Added successfully product category ID: " + brand.getId()));
        }catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("message", string.append("Failed to add because duplicate name"));
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Error Server"));
        }
        return "redirect:/brands/0";
    }

    @RequestMapping(value = "/findByIdBrand", method = {RequestMethod.GET, RequestMethod.PUT})
    @ResponseBody
    public BrandEntity findById(int id) {
        return brandService.getObjectById(id);
    }

    @GetMapping("/update-brand")
    public String updateBrand(BrandEntity brand, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        if(brand.getId() != 0){
            try {
                brandService.updateObject(brand);
                attributes.addFlashAttribute("message", string.append("Updated successfully brand ID: " + brand.getId()));
            } catch (DataIntegrityViolationException e) {
                attributes.addFlashAttribute("message", string.append("Failed to update because duplicate name"));
            }catch (Exception e) {
                e.printStackTrace();
                attributes.addFlashAttribute("message", string.append("Error Server"));
            }
        }
        return "redirect:/brands/0";
    }

    @RequestMapping(value = "/delete-brand", method = {RequestMethod.GET, RequestMethod.PUT})
    public String deleteBrand(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            brandService.deleteObject(id);
            attributes.addFlashAttribute("message", string.append("Deleted successfully brand ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/brands/0";
    }

    @RequestMapping(value = "/enable-brand", method = {RequestMethod.GET, RequestMethod.PUT})
    public String enableBrand(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            brandService.enableObject(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully brand ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/brands/0";
    }






    //--------------------------------------------------------------------------------

    @RequestMapping("/manager_brand")
    public String showBrandManagerPage(Model model,
                                          HttpSession session){
        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/brand_list";
    }

    @RequestMapping("/manager_brand_test")
    public String showBrandManagerPageTest(Model model,
                                       HttpSession session){
        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/tables";
    }

    @RequestMapping("/manager_add_brand")
    public String addBrandManagerPage(Model model,
                                       HttpSession session){
//        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/brand_add";
    }

    @PostMapping("/save_brand_action")
    public String addBrand(@ModelAttribute("brand") BrandEntity brand){
        if(brand.getId() != 0){
            brand.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            brand.setStatus(1);
            brandService.updateObject(brand);
        }else{
            brand.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            brand.setStatus(1);
            brandService.insertObject(brand);
            }
        return "redirect:/brand/manager_brand";
//        return "/admin/brand_list";
    }

    @RequestMapping("/update_brand")
    public String showUpdateBrandPage(@RequestParam("brandId")int brandId,
                                         Model model,
                                         HttpSession session){
        model.addAttribute("brand",brandService.getObjectById(brandId));
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "admin/brand_info";

    }

    @RequestMapping("/delete_brand")
    public String deleteBrand(@RequestParam("brandId")int brandId) {
        if(validateDelete(brandId)){
            brandService.deleteObject(brandId);
        }
        return "redirect:/brand/manager_brand";
    }

    private boolean validateDelete(int brandId){
        if(brandService.getAll().stream().map(BrandEntity::getId).collect(Collectors.toList()).contains(brandId)){
            return true;
        }else{
            errors.add("Brand không tồn tại");
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
