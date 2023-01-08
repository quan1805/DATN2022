package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.BrandDto;
import huce.it.datnbackend.dto.ProductCategoryDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.*;
import huce.it.datnbackend.services.brand.IBrandService;
import huce.it.datnbackend.services.product.IProductService;
import huce.it.datnbackend.services.productcategory.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private IProductService productService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private IProductCategoryService categoryService;


    @RequestMapping(value = {"/index", ""}, method = RequestMethod.GET)
    public String HomePage(Principal principal,
                           HttpSession session, Model model) {
        if(principal != null){
            CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
            ShoppingCartEntity cart = customer.getCart();
            if (cart == null ){

            }
            session.setAttribute("totalItems", cart.getTotalItem());
        }
        return "customer/home";
    }

    @GetMapping("/home")
    public String index(Model model){
        List<ProductCategoryDto> categories = categoryService.getCategoryAndProduct();
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "customer/index";
    }


    @GetMapping("/brand")
    public String brand(Model model){
        List<BrandDto> brands = brandService.getBrandAndProduct();
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("brands", brands);
        model.addAttribute("products", products);
        return "customer/brand";
    }

    @GetMapping("/contact")
    public String contact(){
        return "customer/contact-us";
    }

}
