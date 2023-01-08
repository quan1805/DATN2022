package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.OrderShopDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.*;
import huce.it.datnbackend.services.order.IOrderService;
import huce.it.datnbackend.services.orderdetail.IOrderDetailService;
import huce.it.datnbackend.services.ordershop.IOrderShopService;
import huce.it.datnbackend.services.productdetail.IProductDetailService;
import huce.it.datnbackend.services.shoppingcart.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IOrderShopService orderShopService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @GetMapping("/check-out")
    public String checkOut(Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
        if(customer.getPhone().trim().isEmpty() || customer.getAddress().trim().isEmpty()){

            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "customer/my-account";
        }else{
            model.addAttribute("customer", customer);
            ShoppingCartEntity cart = shoppingCartService.getObjectByCustomer(customer.getId());
            model.addAttribute("cart", cart);
        }
        return "customer/checkout";
    }

    @GetMapping("/my-order")
    public String myOrder( Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        CustomerEntity customer = (CustomerEntity) session.getAttribute("user");
        ShoppingCartEntity shoppingCart = shoppingCartService.getObjectByCustomer(customer.getId());
        OrderEntity order = orderService.saveOrder(shoppingCart);


        List<OrderShopDto> orderShopDtos = orderShopService.getObjectByOrderId(order.getId());
        model.addAttribute("orders", orderShopDtos);
        return "customer/order";

    }

    @GetMapping("/cus-order")
    public String cusOrder(Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        CustomerEntity customer = (CustomerEntity) session.getAttribute("user");

        List<OrderEntity> orders = orderService.getOrderByCustomer(customer.getId());
        model.addAttribute("orders", orders);
        return "customer/order";
    }


    @GetMapping("/orders")
    public String orders(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<OrderDto>orderDtoList = orderService.findAll();
        model.addAttribute("orders", orderDtoList);
        model.addAttribute("size", orderDtoList.size());
        model.addAttribute("title", "Order");
        return "admin/orders";
    }
    @GetMapping("/orders/{pageNo}")
    public String orderPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<OrderDto> orders = orderService.pageOrders(pageNo);
        model.addAttribute("title", "Order");
        model.addAttribute("size", orders.getSize());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/search-result-order/{pageNo}")
    public String searchOrders(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model, Principal principal, RedirectAttributes attributes){
        if (principal == null){
            return "redirect:/login";
        }
        StringBuilder string = new StringBuilder();
        try{
            Page<OrderDto> orders = orderService.searchOrders(pageNo, Integer.valueOf(keyword));
            model.addAttribute("title", "Order");
            model.addAttribute("size", orders.getSize());
            model.addAttribute("totalPages", orders.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("orders", orders);
            model.addAttribute("keyword", keyword);
            return "admin/result-orders";
        }catch (NumberFormatException e){
            attributes.addFlashAttribute("message", string.append("Please enter number"));
            e.printStackTrace();
            return "redirect:/orders/0";
        }
        catch (Exception e) {
            attributes.addFlashAttribute("message", string.append("Failed Search"));
            e.printStackTrace();
            return "redirect:/orders/0";
        }
    }

    @GetMapping("/view-order/{id}")
    public String viewOrder(@PathVariable("id") int id,
                            Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Order");
        OrderDto orderDto = orderService.getById(id);
        List<OrderShopDto> orderShopDtoList = orderShopService.getObjectByOrderId(id);
        model.addAttribute("order", orderDto);
        model.addAttribute("orderShops", orderShopDtoList);

        return "admin/view-order";
    }

    @RequestMapping(value = "/cancel-order/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(@PathVariable("id") int id, RedirectAttributes attributes){
            StringBuilder string = new StringBuilder();
            try{
                orderService.cancelOrder(id);
                attributes.addFlashAttribute("message", string.append("Cancel successfully order ID: " + id));

            }catch (Exception e){
                attributes.addFlashAttribute("message", string.append("Failed"));
                e.printStackTrace();
            }
            return "redirect:/orders/0";
    }






    //------------------------------------------------------------
    @RequestMapping("/manage_order_all")
    public String showAllOrderManagerPage(@RequestParam(value = "pageNumber",required = false, defaultValue = "1")  int pageNumber,
                                       Model model,
                                       HttpSession session){
        if (session.getAttribute("user") instanceof BrandEntity) {
            BrandEntity brand = (BrandEntity) session.getAttribute("user");
            model.addAttribute("orders",orderService.getPage(pageNumber));
        }
        else {
            model.addAttribute("orders",orderService.getPage(pageNumber));
        }
        model.addAttribute("status", 0);
        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/order_list" ;
    }



    @RequestMapping("/manage_order")
    public String showOrderManagerPage(@RequestParam(value = "pageNumber",required = false, defaultValue = "1")  int pageNumber,
                                       @RequestParam(value = "status",required = false, defaultValue = "1") int status,
                                       Model model,
                                       HttpSession session){
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        if (status != 0) {
            model.addAttribute("orders", orderService.getPage(status, pageNumber));
        }
        else {
            model.addAttribute("orders", orderService.getPage(pageNumber));
        }
        model.addAttribute("status", status);
        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/order_list" ;
    }


    @PostMapping("/save_order_action")
    public String addOrder(@ModelAttribute("product") ProductEntity product){
//        if(product.getId() != 0){
//            product.setUpdateDate(new Timestamp(System.currentTimeMillis()));
//            product.setStatus(1);
//            orderService.updateObject(product);
//        }else{
//            product.setCreateDate(new Timestamp(System.currentTimeMillis()));
//            product.setStatus(1);
//            productService.insertObject(product);
//        }
        return "redirect:/manage_order";
    }

    @RequestMapping("/update_order")
    public String showUpdateOrderPage(@RequestParam(value = "orderId", required = false) int orderId,
                                        Model model,
                                        HttpSession session){
        model.addAttribute("order",orderService.getObjectById(orderId));
        model.addAttribute("orderdetails", orderDetailService.getObjectByOrderId(orderId));
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/brand/order_info";

    }
    @RequestMapping("/update_status_order")
    public String updateStatusOrder(@RequestParam(value = "orderId", required = false) int orderId,
                                    @RequestParam(value = "status", required = false) int status,
                                      Model model,
                                      HttpSession session){
        OrderEntity order = orderService.getObjectById(orderId);
        order.setStatus(status);
        order.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        orderService.updateObject(order);
        sentError(model);
        return "redirect:/manage_order_all";

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
