package com.MyProject1.controller;

import com.MyProject1.config.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenController {


    @Autowired
    private SessionManager session;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        if(session.is_login()){
            Object current_page = session.getAttribute("current_page");
            System.out.println(current_page);
            String redirect_url = "redirect:" + (current_page != null ? current_page.toString() : "/");
            return redirect_url;
        }

        return "ui/authen/dangnhap";
    }

    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    public String Authenticated(){

        Object current_page = session.getAttribute("current_page");
        String redirect_url = "redirect:" + (current_page != null ? current_page.toString() : "/");
        System.err.println("authenticated");
        session.set_session_token();
        return redirect_url;
    }

    @RequestMapping(value = "/logouted", method = RequestMethod.GET)
    public String Logouted(){

        Object current_page = session.getAttribute("current_page");
        String redirect_url = "redirect:" + (current_page != null ? current_page.toString() : "/");
        System.err.println("logouted");
        session.del_session_token();
        return redirect_url;
    }
}
