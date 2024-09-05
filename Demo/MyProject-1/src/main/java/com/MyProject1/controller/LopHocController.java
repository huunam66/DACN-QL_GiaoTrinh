package com.MyProject1.controller;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.config.support.Maker;
import com.MyProject1.entity.GiaoTrinh;
import com.MyProject1.entity.Lop;
import com.MyProject1.entity.NhaCungCap;
import com.MyProject1.service.LopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class LopHocController {

    private final String login_page = "redirect:/login";
    private final int limit_item = 5;

    private final String group = "cungcap";

    @Autowired
    private SessionManager session;

    @Autowired
    private LopService lopService;

    @GetMapping("/lophoc")
    public String lophoc(Model model,
                             @RequestParam(name = "search", defaultValue = "") String search,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "sort", defaultValue = "") String sort) {


//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        if(page < 1) page = 1;

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/lophoc");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("search", search);
        List<Lop> lops = lopService.findAll().stream().filter(
                item -> (
                        item.getTen().toLowerCase().contains(search.toLowerCase().trim())
                        ||
                                item.getGiangvien().toLowerCase().contains(search.toLowerCase().trim())
                        ||
                                item.getMonhoc().toLowerCase().contains(search.toLowerCase().trim())
                        ||
                                item.getPhonghoc().toLowerCase().contains(search.toLowerCase().trim())
                )
        ).sorted(Comparator.comparing(Lop::getId).reversed()).toList();
        if(sort.equals("asc")){
            lops = lopService.findAll().stream().sorted(Comparator.comparing(Lop::getTen)).toList();
        }
        if(sort.equals("desc")){
            lops = lopService.findAll().stream().sorted(Comparator.comparing(Lop::getTen).reversed()).toList();
        }

        model.addAttribute("title", "Lớp học");

        int max_size = lops.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);

        lops = lops.stream().skip((page - 1) * 5).limit(limit_item).toList();
        model.addAttribute("sort", sort);

        model.addAttribute("count_item", lops.size());
        model.addAttribute("lops", lops);

        return "ui/lop/lophoc";
    }

    @GetMapping("/lophoc/create")
    public String book_create(Model model, Lop lophoc){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/lophoc/create");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Thêm mới lớp học");

        model.addAttribute("lophoc", lophoc);

        model.addAttribute("action", "/lophoc/create");

        if(session.getAttribute("status_model") != null){
            model.addAttribute("lophoc", session.getAttribute("status_model"));
            model.addAttribute("contraint_name", session.getAttribute("contraint_name"));
            model.addAttribute("status_save", session.getAttribute("status_save"));
            session.removeAttribute("contraint_name");
            session.removeAttribute("status_model");
            session.removeAttribute("status_save");
        }

        return "ui/lop/capnhatlophoc";
    }

    @RequestMapping(value = "/lophoc/create", method = RequestMethod.POST)
    public String book_create_post(@ModelAttribute Lop lophoc){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================
        Optional<Lop> gt = lopService.findAll().stream()
            .filter(item -> item.getId() != lophoc.getId() && item.getTen().equals(lophoc.getTen()))
            .findFirst();

        if(gt.isPresent()){
            session.setAttribute("contraint_name", "Trùng tên !");
            session.setAttribute("status_save", "Thêm thất bại !");
        }
        else {
            try {
                lophoc.setId(0);
                lopService.save(lophoc);
                session.setAttribute("status_save", "Thêm mới thành công !");
            } catch (Exception e) {
                session.setAttribute("status_save", "Thêm mới thất bại !");
                System.err.println(e);
            }
        }

        session.setAttribute("status_model", lophoc);

        return "redirect:/lophoc/create";
    }

    @RequestMapping(value = "/lophoc/update", method = RequestMethod.GET)
    public String book_update(Model model, @RequestParam(name = "id") int id){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        session.setAttribute("current_page", null);

        model.addAttribute("fullname", session.get_session_fullname_token());

        String title = "Cập nhật lớp học";
        model.addAttribute("title", title);

        model.addAttribute("action", "/lophoc/update");


        Lop lophoc = lopService.findById(id);
        model.addAttribute("lophoc", lophoc);

        if(session.getAttribute("status_model") != null){
            model.addAttribute("lophoc", session.getAttribute("status_model"));
            model.addAttribute("contraint_name", session.getAttribute("contraint_name"));
            model.addAttribute("status_save", session.getAttribute("status_save"));
            session.removeAttribute("contraint_name");
            session.removeAttribute("status_model");
            session.removeAttribute("status_save");
        }

        return "ui/lop/capnhatlophoc";
    }


    @RequestMapping(value = {"/lophoc/update"}, method = RequestMethod.POST)
    public String book_update_post(@ModelAttribute Lop lophoc){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        Optional<Lop> gt = lopService.findAll().stream()
                .filter(item -> item.getId() != lophoc.getId() && item.getTen().equals(lophoc.getTen()))
                .findFirst();

        if(gt.isPresent()){
            session.setAttribute("contraint_name", "Trùng tên !");
            session.setAttribute("status_save", "Cập nhật thất bại !");
        }
        else{
            try{
                lopService.save(lophoc);
                session.setAttribute("status_save", "Cập nhật thành công !");
            }
            catch (Exception e){
                session.setAttribute("status_save", "Cập nhật thất bại !");
                System.err.println(e);
            }
        }
        session.setAttribute("status_model", lophoc);
        return "redirect:/lophoc/update?id="+lophoc.getId();
    }

    @RequestMapping(value = "/lophoc/delete/{id}", method = RequestMethod.GET)
    public String xoalophoc(@PathVariable("id") int id) {

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        lopService.deleteById(id);
        return "redirect:/lophoc";
    }

}
