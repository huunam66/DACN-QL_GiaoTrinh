package com.MyProject1.controller;

import com.MyProject1.config.support.Maker;
import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.NhaCungCap;
import com.MyProject1.service.NhaCungCapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NhaCungCapController {

    private final String login_page = "redirect:/login";
    private final int limit_item = 5;

    private final String group = "cungcap";

    @Autowired
    private SessionManager session;

    @Autowired
    private NhaCungCapService nhaCungCapService;

    @GetMapping("/nhacungcap")
    public String nhacungcap(Model model,
                             @RequestParam(value = "search", defaultValue = "") String search,
                             @RequestParam(name = "page", defaultValue = "1") int page) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================


         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);


        model.addAttribute("group", group);
        session.setAttribute("current_page", "/nhacungcap");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Nhà cung cấp");
        List<NhaCungCap> nccs = nhaCungCapService.findAll().stream().filter(
                item -> item.getTen().toLowerCase().contains(search.toLowerCase())
        ).toList();
        int max_size = nccs.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);

        nccs = nccs.stream().skip((page - 1) * 5).limit(limit_item).toList();
        System.err.println(nccs.size());
        model.addAttribute("count_item", nccs.size());
        model.addAttribute("nccs", nccs);
        model.addAttribute("search", search);

        return "ui/supplier/nhacungcap";
    }

    @GetMapping("/nhacungcap/create")
    public String themnhacungcap(Model model) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        model.addAttribute("create_nhaCungCap", true);

        session.setAttribute("current_page", "/nhacungcap/create");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Thêm nhà cung cấp");
        NhaCungCap nhaCungCap = new NhaCungCap();
        model.addAttribute("nhaCungCap", nhaCungCap);
        return "ui/supplier/themnhacungcap";
    }

    @GetMapping("/nhacungcap/update")
    public String chinhsuanhacungcap(Model model, int id) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================


         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);


        model.addAttribute("group", group);
        model.addAttribute("update_nhaCungCap", true);

        session.setAttribute("current_page", "/nhacungcap/update");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Chỉnh sửa nhà cung cấp");
        NhaCungCap nhaCungCap = nhaCungCapService.findById(id);
        model.addAttribute("nhaCungCap", nhaCungCap);
        return "ui/supplier/themnhacungcap";
    }

    @GetMapping("/nhacungcap/delete")
    public String xoanhacungcap(int id) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        nhaCungCapService.deleteById(id);
        return "redirect:/nhacungcap";
    }

    @PostMapping("/nhacungcap/save")
    public String luunhacungcap(@ModelAttribute("nhaCungCap") NhaCungCap nhaCungCap) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        nhaCungCapService.save(nhaCungCap);
        return "redirect:/nhacungcap";
    }

}
