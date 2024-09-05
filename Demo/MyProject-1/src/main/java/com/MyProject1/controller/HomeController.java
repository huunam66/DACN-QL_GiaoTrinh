package com.MyProject1.controller;

import com.MyProject1.config.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    private final String login_page = "redirect:/login";
    private final String group = "home";

//    @Autowired
//    @Qualifier("inMemoryUserDetailsManager")
//    private UserDetailsManager userDetailsManager;

    @Autowired
    private SessionManager session;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(Model model) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);

        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "HUIT");

        return "ui/home/index";
    }

    @GetMapping("/test2")
    public String test2(){
        return "ui/home/index";
    }
}
