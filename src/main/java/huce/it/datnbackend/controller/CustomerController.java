package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.services.customer.ICustomerService;
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
@RequestMapping("/customer")
public class CustomerController {

    private List<String> errors = new ArrayList<>();

    @Autowired
    ICustomerService customerService;

    @GetMapping("/create")
    public String create() {
        return "customer/createCustomer";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CustomerEntity customerEntity) {
        customerService.insertObject(customerEntity);
        return "customer/home";
    }

    @RequestMapping("/manager_customer")
    public String showCustomerManagerPage(Model model,
                                       HttpSession session){
        model.addAttribute("customers",customerService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/customer_list";
    }

    @RequestMapping("/manager_add_customer")
    public String addCustomerManagerPage(Model model,
                                      HttpSession session){
//        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/customer_add";
    }

    @PostMapping("/save_customer_action")
    public String addCustomer(@ModelAttribute("customer") CustomerEntity customer){
        if(customer.getId() != 0){
            customer.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            customer.setStatus(1);
            customerService.updateObject(customer);
        }else{
            customer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            customer.setStatus(1);
            customerService.insertObject(customer);
        }
        return "redirect:/customer/manager_customer";
    }

    @RequestMapping("/update_customer")
    public String showUpdateCustomerPage(@RequestParam("customerId") int customerId,
                                      Model model,
                                      HttpSession session){
        model.addAttribute("customer", customerService.getObjectById(customerId));
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "admin/customer_info";

    }

    @RequestMapping("/delete_customer")
    public String deleteCustomer(@RequestParam("customerId")int customerId) {
        if(validateDelete(customerId)){
            customerService.deleteObject(customerId);
        }
        return "redirect:/customer/manager_customer";
    }

    private boolean validateDelete(int customerId){
        if(customerService.getAll().stream().map(CustomerEntity::getId).collect(Collectors.toList()).contains(customerId)){
            return true;
        }else{
            errors.add("Customer không tồn tại");
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
