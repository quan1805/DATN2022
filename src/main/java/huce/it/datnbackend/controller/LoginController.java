package huce.it.datnbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String showLogin(Model model,
                            @RequestParam(required = false) String error){
        model.addAttribute("error", error);
        return "/login";
    }

//    @RequestMapping("/manager")
//    public String showManagerPage(){
//        return "/html/Manager/manager-index";
//    }
}
