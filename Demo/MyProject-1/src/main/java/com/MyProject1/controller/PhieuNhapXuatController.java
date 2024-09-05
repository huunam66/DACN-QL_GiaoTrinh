package com.MyProject1.controller;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.ChiTietPhieuNhap;
import com.MyProject1.entity.NhaCungCap;
import com.MyProject1.entity.NhanVien;
import com.MyProject1.entity.PhieuNhap;
import com.MyProject1.service.PhieuNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PhieuNhapXuatController {

    private final String login_page = "redirect:/login";
    private final String group = "kho";

    @Autowired
    private SessionManager session;

    @Autowired
    private PhieuNhapService phieuNhapService;

    @GetMapping("/phieunhap/create")
    public String taophieunhap(Model model) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        model.addAttribute("create_nhap_xuat", true);
        session.setAttribute("current_page", "/phieunhap/create");
        model.addAttribute("fullname", session.get_session_fullname_token());
        model.addAttribute("id", session.get_session_id_token());

        model.addAttribute("title", "Nhập giáo trình");

        return "ui/phieu/taophieunhap";
    }

    @GetMapping("/phieuxuat/create")
    public String taophieuxuat(Model model) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        model.addAttribute("create_nhap_xuat", true);
        model.addAttribute("id", session.get_session_id_token());
        session.setAttribute("current_page", "/phieuxuat/create");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Xuất giáo trình");
        return "ui/phieu/taophieuxuat";
    }



    @GetMapping("/phieu/thongke")
    public String thongke(Model model)
    {
//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", "phieu");
        session.setAttribute("current_page", "/phieu/thongke");

        model.addAttribute("fullname", session.get_session_fullname_token());
        model.addAttribute("title", "Thống kê phiếu");
        return "ui/phieu/thongkephieu";
    }

}
