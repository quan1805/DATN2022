package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.services.brand.IBrandService;
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
//@RequestMapping("/brand")
public class BrandController {

    private List<String> errors = new ArrayList<>();

    @Autowired
    IBrandService brandService;

    @GetMapping("/search")
    public String search(Model model) {
        List<BrandEntity> brands = brandService.getAll();
        model.addAttribute("uList", brands);

        return "brand/searchBrand";
    }

    @GetMapping("/create")
    public String create() {
        return "brand/createBrand";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute BrandEntity brand) {
        brandService.insertObject(brand);
        return "brand/home";
    }

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
