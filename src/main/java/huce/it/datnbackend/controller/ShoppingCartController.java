package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.CartItemEntity;
import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ShoppingCartEntity;
import huce.it.datnbackend.services.productdetail.IProductDetailService;
import huce.it.datnbackend.services.shoppingcart.IShoppingCartService;
import huce.it.datnbackend.services.shoppingcart.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IProductDetailService productDetailService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        if (principal == null) {
            return "redirect:/login";
        }
        CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
        ShoppingCartEntity shoppingCart = shoppingCartService.getObjectByCustomer(customer.getId());
        if (shoppingCart == null) {
            model.addAttribute("check", "No item in your cart");
        }

        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("subTotal", shoppingCart.getTotalPrice());
        session.setAttribute("totalItems", shoppingCart.getTotalItem());

        return "customer/cart";
    }

    @PostMapping ("add-to-cart")
    public String addItemToCart(
            @ModelAttribute("cartItem") CartItemEntity cartItem,
//            @RequestParam("id") int productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            HttpSession session,
            Model model,
            HttpServletRequest request){

        if (principal == null){
            return "redirect:/login";
        }
        ProductDetailEntity productDetail = productDetailService.getObjectById(cartItem.getProductDetail().getId());
        CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
        ShoppingCartEntity cart = shoppingCartService.addItemToCart(productDetail, cartItem.getQuantity(), productDetail.getProduct().getBrand(), customer);

        return "redirect:" + request.getHeader("Referer");

    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") int productId,
                             Model model,
                             Principal principal,
                             HttpSession session
                             ){

        if(principal == null){
            return "redirect:/login";
        }else{
            ProductDetailEntity productDetail = productDetailService.getObjectById(productId);
            CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
            ShoppingCartEntity cart = shoppingCartService.updateItemInCart(productDetail, quantity, productDetail.getProduct().getBrand(), customer);

            model.addAttribute("shoppingCart", cart);
            model.addAttribute("subTotal", cart.getTotalPrice());

            return "redirect:/cart";
        }

    }


    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItemFromCart(@RequestParam("id") int productId,
                                     Model model,
                                     Principal principal, HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }else{
            ProductDetailEntity productDetail = productDetailService.getObjectById(productId);
            CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
            ShoppingCartEntity cart = shoppingCartService.deleteItemFromCart(productDetail, productDetail.getProduct().getBrand(), customer);
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("subTotal", cart.getTotalPrice());
            return "redirect:/cart";
        }

    }



}
