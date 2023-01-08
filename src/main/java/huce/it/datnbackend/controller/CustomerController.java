package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.services.account.IAccountService;
import huce.it.datnbackend.services.brand.IBrandService;
import huce.it.datnbackend.services.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/customer")
public class CustomerController {

    private List<String> errors = new ArrayList<>();

    @Autowired
    ICustomerService customerService;

    @Autowired
    IAccountService accountService;

    @Autowired
    IBrandService brandService;

    @GetMapping("/account")
    public String myAccount(HttpSession session, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }else {
            return "/customer/my-account";
        }
    }

    @RequestMapping("/customer/home")
    public String showCustomerHomePage(Model model,
                                       HttpSession session){
        sentError(model);
        return "/customer/home";
    }

//    @GetMapping("/create")
//    public String create() {
//        return "customer/createCustomer";
//    }
//
//    @PostMapping("/create")
//    public String create(@ModelAttribute CustomerEntity customerEntity) {
//        customerService.insertObject(customerEntity);
//        return "customer/home";
//    }

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
    public String addCustomer(@ModelAttribute("customer") CustomerEntity customer,
                              @ModelAttribute("account") AccountEntity account){
        if(customer.getId() != 0){
            customer.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            customer.setStatus(1);
            customerService.updateObject(customer);
        }else{
            customer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            customer.setStatus(1);
            int customerId = customerService.insertObject(customer);

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordBcrypt = passwordEncoder.encode(account.getPassword());
            account.setPassword(passwordBcrypt);
            account.setCustomer(customerService.getObjectById(customerId));
            BrandEntity brand = brandService.getObjectById(0);
            account.setBrand(brand);
            account.setRole(2);
            account.setStatus(1);
            account.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            accountService.insertObject(account);
        }
        return "redirect:/login";
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
