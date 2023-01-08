package huce.it.datnbackend.controller;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.services.account.IAccountService;
import huce.it.datnbackend.services.brand.IBrandService;
import huce.it.datnbackend.services.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {
    private List<String> errors = new ArrayList<>();

    @Autowired
    IAccountService accountService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    IBrandService brandService;
    @RequestMapping("/accounts")
    public String showAccountPage(Model model,
                                          HttpSession session, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        List<AccountEntity> accounts = accountService.getAll();
        model.addAttribute("accounts",accounts);
        model.addAttribute("size",accounts.size());
        model.addAttribute("title","Account");

        model.addAttribute("accountNew", new AccountEntity());
//        model.addAttribute("user",session.getAttribute("user"));
        sentError(model);
        return "/admin/accounts";
    }

    @GetMapping("/accounts/{pageNo}")
    public String accountPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<AccountEntity> accounts = accountService.pageAccounts(pageNo);
        List<BrandEntity> brands = brandService.getAll();
        List<CustomerEntity> customers = customerService.getAll();

        model.addAttribute("title", "Account");
        model.addAttribute("size", accounts.getSize());
        model.addAttribute("totalPages", accounts.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("accounts", accounts);
        model.addAttribute("brands", brands);
        model.addAttribute("customers", customers);
        model.addAttribute("accountNew", new AccountEntity());
        return "/admin/accounts";
    }

    @GetMapping("/search-result-account/{pageNo}")
    public String searchAccounts(@PathVariable("pageNo") int pageNo,
                                   @RequestParam("keyword") String keyword,
                                   Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<AccountEntity> accounts = accountService.searchAccounts(pageNo, keyword);
        model.addAttribute("title", "Account");
        model.addAttribute("size", accounts.getSize());
        model.addAttribute("totalPages", accounts.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountNew", new AccountEntity());
        model.addAttribute("keyword", keyword);

        return "admin/result-accounts";

    }
    @PostMapping("/add-account")
    public String addAccount(@ModelAttribute("accountNew") AccountEntity account,
                                     RedirectAttributes attributes,
                                     Model model){
        StringBuilder string = new StringBuilder();
        try {
            account.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            account.setStatus(1);
            if (account.getCustomer() == null || account.getCustomer().getId() == 0) {
                account.setCustomer(customerService.getObjectById(0));
            }
            if (account.getBrand() == null || account.getBrand().getId() == 0) {
                account.setBrand(brandService.getObjectById(0));
            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordBcrypt = passwordEncoder.encode(account.getPassword());
            account.setPassword(passwordBcrypt);
            accountService.insertObject(account);
            attributes.addFlashAttribute("message", string.append("Added successfully account ID: " + account.getId()));
        }catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("message", string.append("Failed to add because duplicate username name"));
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Error Server"));
        }
        return "redirect:/accounts/0";
    }

    @RequestMapping(value = "/findByAccountId", method = {RequestMethod.GET, RequestMethod.PUT})
    @ResponseBody
    public AccountEntity findById(int id) {
        return accountService.getObjectById(id);
    }

    @GetMapping("/update-account")
    public String updateAccount(AccountEntity account, RedirectAttributes attributes){
        StringBuilder string = new StringBuilder();
        if(account.getId() != 0){
            try {
                accountService.updateObject(account);
                attributes.addFlashAttribute("message", string.append("Updated successfully account ID: " + account.getId()));
            } catch (DataIntegrityViolationException e) {
                attributes.addFlashAttribute("message", string.append("Failed to update because duplicate name"));
            }catch (Exception e) {
                e.printStackTrace();
                attributes.addFlashAttribute("message", string.append("Error Server"));
            }
        }
        return "redirect:/accounts/0";
    }

    @RequestMapping(value = "/delete-account", method = {RequestMethod.GET, RequestMethod.PUT})
    public String deleteAccount(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            accountService.deleteObject(id);
            attributes.addFlashAttribute("message", string.append("Deleted successfully account ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/accounts/0";
    }

    @RequestMapping(value = "/enable-account", method = {RequestMethod.GET, RequestMethod.PUT})
    public String enableAccount(int id, RedirectAttributes attributes) {
        StringBuilder string = new StringBuilder();
        try {
            accountService.enableObject(id);
            attributes.addFlashAttribute("message", string.append("Enable successfully account ID: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message", string.append("Failed"));
        }
        return "redirect:/accounts/0";
    }



    //-----------------------------
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
    public String addAccount(@ModelAttribute("account") AccountEntity account){
        if(account.getId() != 0){
            account.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            account.setStatus(1);
            accountService.updateObject(account);
        }else{
            account.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            account.setStatus(1);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordBcrypt = passwordEncoder.encode(account.getPassword());
            account.setPassword(passwordBcrypt);
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
