package huce.it.datnbackend.controller;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.OrderShopDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import huce.it.datnbackend.services.orderdetail.IOrderDetailService;
import huce.it.datnbackend.services.ordershop.IOrderShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class OrderShopController {

    @Autowired
    private IOrderShopService orderShopService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @GetMapping("/view-order-shop/{id}")
    public String viewOrderShop(@PathVariable("id") int id,
                            Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Order Shop");
        List<OrderDetailDto> orderDetailDtoList = orderDetailService.getOrderDetailByOrderId(id);
        model.addAttribute("orderShops", orderDetailDtoList);

        return "admin/view-order-shop";
    }

    @GetMapping("/brand-orders/{pageNo}")
    public String brandOrderPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        BrandEntity brand = (BrandEntity) session.getAttribute("user");
        Page<OrderShopDto> orders = orderShopService.pageOrdersBrand(pageNo, brand.getId());
        model.addAttribute("title", "Order");
        model.addAttribute("size", orders.getSize());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("orders", orders);
        return "brand/orders";
    }

    @GetMapping("/brand-search-result-order/{pageNo}")
    public String brandSearchOrders(@PathVariable("pageNo") int pageNo,
                               @RequestParam("keyword") String keyword,
                               Model model, Principal principal, RedirectAttributes attributes, HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }
        StringBuilder string = new StringBuilder();
        try{
            BrandEntity brand = (BrandEntity) session.getAttribute("user");
            Page<OrderShopDto> orders = orderShopService.searchOrders(pageNo, Integer.valueOf(keyword), brand.getId());
            model.addAttribute("title", "Order");
            model.addAttribute("size", orders.getSize());
            model.addAttribute("totalPages", orders.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("orders", orders);
            model.addAttribute("keyword", keyword);
            return "brand/result-orders";
        }catch (NumberFormatException e){
            attributes.addFlashAttribute("message", string.append("Please enter number"));
            e.printStackTrace();
            return "redirect:/brand-orders/0";
        }
        catch (Exception e) {
            attributes.addFlashAttribute("message", string.append("Failed Search"));
            e.printStackTrace();
            return "redirect:/brand-orders/0";
        }
    }

    @GetMapping("/brand-view-order/{id}")
    public String brandViewOrder(@PathVariable("id") int id,
                            Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Order");
        OrderShopDto orderDto = orderShopService.getObjectDtoById(id);
        List<OrderDetailDto> orderDetailDtoList = orderDetailService.getOrderDetailByOrderId(id);
        model.addAttribute("order", orderDto);
        model.addAttribute("orderDetails", orderDetailDtoList);

        return "brand/view-order";
    }
    @RequestMapping("/brand-update_status_order")
    public String brandUpdateStatusOrder(@RequestParam(value = "orderId", required = false) int orderId,
                                    @RequestParam(value = "status", required = false) int status,
                                    Model model,
                                    HttpSession session,
                                    Principal principal,
                                    RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();

        if (principal == null){
            return "redirect:/login";
        }
        OrderShopEntity order = orderShopService.getObjectById(orderId);
        order.setStatus(status);
        order.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        orderShopService.updateObject(order);
        attributes.addFlashAttribute("message", string.append("Update status order success"));
        return "redirect:/brand-orders/0";

    }
}
