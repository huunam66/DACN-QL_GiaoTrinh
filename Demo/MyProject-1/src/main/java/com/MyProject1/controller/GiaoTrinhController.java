package com.MyProject1.controller;

import com.MyProject1.config.support.Maker;
import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.GiaoTrinh;
import com.MyProject1.service.ChuyenNganhService;
import com.MyProject1.service.GiaoTrinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class GiaoTrinhController {

    private final String login_page = "redirect:/login";
    private final String group = "kho";
    private final int limit_item = 5;


    @Autowired
    private SessionManager session;

    @Autowired
    private GiaoTrinhService giaoTrinhService;

    @Autowired
    private ChuyenNganhService chuyenNganhService;


    @RequestMapping("/book")
    public String index(Model model,
                        @RequestParam(name = "search", defaultValue = "") String search,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "sort", defaultValue = "") String sort){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        if(page < 1) page = 1;

        String search_patch = !search.equals("") ? "&search="+search : "";
        String page_patch = page > 1 ? "&page="+page : "";
        String sort_patch = !sort.equals("") ? "&sort="+sort : "";
        session.setAttribute("return_page_book", "/book?"+search_patch+page_patch+sort_patch);

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/book");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("search", search);
        List<GiaoTrinh> books = giaoTrinhService.findAll().stream().filter(
                item -> item.getTen().toLowerCase().contains(search.toLowerCase().trim())
        ).sorted(Comparator.comparing(GiaoTrinh::getId).reversed()).toList();
        if(sort.equals("asc")){
            books = giaoTrinhService.findAll().stream().sorted(Comparator.comparing(GiaoTrinh::getGia)).toList();
        }
        if(sort.equals("desc")){
            books = giaoTrinhService.findAll().stream().sorted(Comparator.comparing(GiaoTrinh::getGia).reversed()).toList();
        }

        model.addAttribute("title", "Kho giáo trình");

        int max_size = books.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);

        books = books.stream().skip((page - 1) * 5).limit(limit_item).toList();
        model.addAttribute("sort", sort);

        model.addAttribute("count_item", books.size());
        model.addAttribute("books", books);

        return "ui/book/khohang";
    }

    @GetMapping("/book/create")
    public String book_create(Model model, GiaoTrinh giaoTrinh){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("return_page_book", session.getAttribute("return_page_book"));

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/book/create");
        model.addAttribute("fullname", session.get_session_fullname_token());


        model.addAttribute("title", "Thêm mới giáo trình");

        model.addAttribute("giaotrinh", giaoTrinh);

        model.addAttribute("list_chuyennganh", chuyenNganhService.List_ChuyenNganh());

        model.addAttribute("action", "/book/create");

        if(session.getAttribute("status_model") != null){
            model.addAttribute("giaotrinh", session.getAttribute("status_model"));
            model.addAttribute("contraint_name", session.getAttribute("contraint_name"));
            model.addAttribute("status_save", session.getAttribute("status_save"));
            model.addAttribute("chuyennganh_index", session.getAttribute("chuyennganh"));
            model.addAttribute("namxuatban_index", session.getAttribute("namxuatban"));
            session.removeAttribute("contraint_name");
            session.removeAttribute("status_model");
            session.removeAttribute("status_save");
            session.removeAttribute("chuyennganh");
            session.removeAttribute("namxuatban");
        }

        return "ui/book/themgiaotrinh";
    }

    @RequestMapping(value = "/book/create", method = RequestMethod.POST)
    public String book_create_post(@ModelAttribute GiaoTrinh giaoTrinh,
                                   @RequestParam(name = "file") MultipartFile file){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

//        Optional<GiaoTrinh> gt = giaoTrinhService.findAll().stream().filter(
//                item -> item.getTen().equals(giaoTrinh.getTen())
//        ).findFirst();
//
//        if(gt.isPresent()){
//            session.setAttribute("contraint_name", "Trùng tên !");
//            session.setAttribute("status_save", "Thêm mới thất bại !");
//            giaoTrinh.setHinhanh(gt.get().getHinhanh());
//        }
//        else{
            try{
                if(file != null && !file.getOriginalFilename().equals("")){
                    Boolean file_upload = Maker.upload_file("/book", file);
                    if(file_upload) giaoTrinh.setHinhanh(file.getOriginalFilename());
                }
                giaoTrinh.setId(0);
                giaoTrinh.setSoluong(0);
                giaoTrinh.setGia(0);
                giaoTrinhService.save(giaoTrinh);
                session.setAttribute("status_save", "Thêm mới thành công !");
            }
            catch (Exception e){
                session.setAttribute("status_save", "Thêm mới thất bại !");
                System.err.println(e);
            }
//        }
        session.setAttribute("chuyennganh", giaoTrinh.getChuyennganh().getId());
        session.setAttribute("namxuatban", giaoTrinh.getNamxuatban());
        session.setAttribute("status_model", giaoTrinh);

        return "redirect:/book/create";
    }

    @RequestMapping(value = {"/book/update", "/book/detail"}, method = RequestMethod.GET)
    public String book_update(Model model, @RequestParam(name = "id") int id){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

         Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);

        model.addAttribute("return_page_book", session.getAttribute("return_page_book"));

        model.addAttribute("group", group);
        session.setAttribute("current_page", null);
        model.addAttribute("fullname", session.get_session_fullname_token());


        String title = session.request_url().contains("detail") ? "Chi tiết giáo trình" : "Cập nhật giáo trình";
        model.addAttribute("title", title);

        model.addAttribute("action", "/book/update");

        model.addAttribute("list_chuyennganh", chuyenNganhService.List_ChuyenNganh());

        GiaoTrinh giaoTrinh = giaoTrinhService.findById(id);
        model.addAttribute("giaotrinh", giaoTrinh);

        model.addAttribute("chuyennganh_index", giaoTrinh.getChuyennganh().getId());
        model.addAttribute("namxuatban_index", giaoTrinh.getNamxuatban());

        if(session.getAttribute("status_model") != null){
            model.addAttribute("giaotrinh", session.getAttribute("status_model"));
            model.addAttribute("contraint_name", session.getAttribute("contraint_name"));
            model.addAttribute("status_save", session.getAttribute("status_save"));
            model.addAttribute("chuyennganh_index", session.getAttribute("chuyennganh"));
            model.addAttribute("namxuatban_index", session.getAttribute("namxuatban"));
            session.removeAttribute("contraint_name");
            session.removeAttribute("status_model");
            session.removeAttribute("status_save");
            session.removeAttribute("chuyennganh");
            session.removeAttribute("namxuatban");
        }

        return "ui/book/themgiaotrinh";
    }


    @RequestMapping(value = {"/book/update"}, method = RequestMethod.POST)
    public String book_update_post(@ModelAttribute GiaoTrinh giaoTrinh,
                                   @RequestParam(name = "file") MultipartFile file){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

//        Optional<GiaoTrinh> gt = giaoTrinhService.findAll().stream()
//                .filter(item -> item.getId() != giaoTrinh.getId() && item.getTen().equals(giaoTrinh.getTen()))
//                .findFirst();
//
//        if(gt.isPresent()){
//            session.setAttribute("contraint_name", "Trùng tên !");
//            session.setAttribute("status_save", "Cập nhật thất bại !");
//        }
//        else{
            try{
                GiaoTrinh gt_find = giaoTrinhService.findById(giaoTrinh.getId());
                giaoTrinh.setHinhanh(gt_find.getHinhanh());
                giaoTrinh.setNgaytao(gt_find.getNgaytao());
                giaoTrinh.setSoluong(gt_find.getSoluong());
                if(file != null && !file.getOriginalFilename().equals("")){
                    Boolean file_upload = Maker.upload_file("/book", file);
                    if(file_upload) giaoTrinh.setHinhanh(file.getOriginalFilename());
                }
                giaoTrinhService.save(giaoTrinh);
                session.setAttribute("status_save", "Cập nhật thành công !");
            }
            catch (Exception e){
                session.setAttribute("status_save", "Cập nhật thất bại !");
                System.err.println(e);
            }
//        }
        session.setAttribute("chuyennganh", giaoTrinh.getChuyennganh().getId());
        session.setAttribute("namxuatban", giaoTrinh.getNamxuatban());
        session.setAttribute("status_model", giaoTrinh);
        return "redirect:/book/update?id="+giaoTrinh.getId();
    }

    @RequestMapping(value = "/book/delete/{id}", method = RequestMethod.GET)
    public String book_delete(@PathVariable(name = "id") int id){

//        ====================================================
        if(!session.is_login()) return login_page;
//        ====================================================

        try{
            giaoTrinhService.deleteById(id);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return "redirect:/book";
    }


    @GetMapping("/book/thongke")
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

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/book/thongke");

        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Thống kê");
        return "ui/book/thongkegiaotrinh";
    }

}
