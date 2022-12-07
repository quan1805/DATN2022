package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.services.account.IAccountService;
import huce.it.datnbackend.services.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    IAccountService accountService;

    @RequestMapping("/manager_account")
    public String showAccountManagerPage(Model model,
                                          HttpSession session) {
        model.addAttribute("accounts", accountService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/account_list";
    }

    @RequestMapping("/manager_add_account")
    public String addAccountManagerPage(Model model,
                                         HttpSession session){
//        model.addAttribute("brands",brandService.getAll());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/account_add";
    }

    @PostMapping("/save_account_action")
    public String addCustomer(@ModelAttribute("account") AccountEntity account){
        if(account.getId() != 0){
            account.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            account.setStatus(1);
            accountService.updateObject(account);
        }else{
            account.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            account.setStatus(1);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountService.insertObject(account);
        }
        return "redirect:/account/manager_account";
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
