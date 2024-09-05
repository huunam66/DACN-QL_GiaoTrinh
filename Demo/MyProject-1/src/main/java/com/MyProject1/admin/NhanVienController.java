package com.MyProject1.admin;

import com.MyProject1.config.support.Maker;
import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.NhanVien;
import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;
import com.MyProject1.service.NhanVienService;
import com.MyProject1.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class NhanVienController {

    private final String group = "nhanvien";
    private final int limit_item = 5;

    @Autowired
    private SessionManager session;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private TaiKhoanService taiKhoanService;


    @RequestMapping(value = "/nhanvien",
                    method = RequestMethod.GET)
    public String nhanvien(Model model,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "search", defaultValue = "") String search,
                           @RequestParam(name = "type", defaultValue = "") String type){

        Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        System.err.println(role);
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/nhanvien");

        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Quản lý nhân viên");

        System.err.println(type);

        List<NhanVien> nhanViens = nhanVienService.List_NhanVien();
        nhanViens = nhanViens.stream().filter(
                item -> item.getTaiKhoan().getQuyen().stream().filter(
                        i -> i.getTen().toLowerCase().contains(type.toLowerCase())
                ).findFirst().isPresent()
        ).toList();

        //tim kiem theo ma hoac ten
        nhanViens = nhanViens.stream().filter(
            item -> (
                item.getTen().toLowerCase().contains(search.toLowerCase())
                    ||
                item.getMssv().toLowerCase().contains(search.toLowerCase())
            )
        ).toList();
        model.addAttribute("search", search);

        List<String> roles= (List<String>)session.get_session_roles_token();
        roles = roles.stream().map(item -> item.toUpperCase()).toList();
        if(roles.contains("ADMIN")){
            nhanViens = nhanViens.stream().filter(
                item -> (
                    item.getTaiKhoan() == null
                        ||
                    (
                        item.getTaiKhoan() != null
                        &&
                        !item.getTaiKhoan().getQuyen().stream().filter(
                            i -> i.getTen().toUpperCase().equals("ADMIN")
                        ).findFirst().isPresent()
                    )
                )
            ).toList();
        }
        else if(roles.contains("MANAGER")){
            nhanViens = nhanViens.stream().filter(
                item -> (
                    item.getTaiKhoan() == null
                        ||
                    (
                        item.getTaiKhoan() != null
                        &&
                        !item.getTaiKhoan().getQuyen().stream().filter(
                                i -> (
                                    i.getTen().toUpperCase().equals("MANAGER")
                                    ||
                                    i.getTen().toUpperCase().equals("ADMIN")
                                )
                        ).findFirst().isPresent()
                    )
                )
            ).toList();
        }


        int len = nhanViens.size();
        boolean[] quyen = new boolean[len];
        for(int i = 0; i < len; i++){
            quyen[i] = nhanViens.get(i).getTaiKhoan().getQuyen().stream().filter(item -> item.getTen().equals("MANAGER")).findFirst().isPresent();
        }
        model.addAttribute("quyen", quyen);

        int max_size = nhanViens.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        nhanViens = nhanViens.stream().skip((page - 1) * 5).limit(limit_item).toList();

        model.addAttribute("count_item", nhanViens.size());
        model.addAttribute("list_nhanvien", nhanViens);

        return "ui/staff/nhanvien";
    }


    @RequestMapping(value = "/nhanvien/create", method = RequestMethod.GET)
    public String nhanvien_create(Model model){


        Boolean admin = session.admin(true);
        Boolean manager = session.admin(true);
        Boolean user = session.admin(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);


        model.addAttribute("group", group);
        session.setAttribute("current_page", "/nhanvien/create");

        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Thêm mới nhân viên");
        NhanVien nhanVien = new NhanVien();
        model.addAttribute("nhanvien", nhanVien);

        model.addAttribute("action", "/nhanvien/create");

        return "ui/staff/taonhanvien";
    }

    @RequestMapping(value = "/nhanvien/update", method = RequestMethod.GET)
    public String nhanvien_update(Model model, int id){

        Boolean admin = session.admin(true);
        Boolean manager = session.admin(true);
        Boolean user = session.admin(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        model.addAttribute("role", role);


        model.addAttribute("group", group);

        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Chỉnh sửa nhân viên");
        NhanVien nhanVien = nhanVienService.findById(id);
        model.addAttribute("nhanvien", nhanVien);

        model.addAttribute("action", "/nhanvien/save");

        return "ui/staff/taonhanvien";
    }

    @PostMapping("/nhanvien/create")
    public String nhanvien_create(@ModelAttribute("nhanvien") NhanVien nhanVien, Model model){
        NhanVien nv = nhanVienService.findByMssv(nhanVien.getMssv());
        if(nv != null)
        {
            model.addAttribute("status_save", "Mã số sinh viên đã tồn tại");
            return "ui/staff/taonhanvien";
        }

        nhanVien = nhanVienService.save(nhanVien);
        System.err.println(nhanVien);
        if(nhanVien != null){
            taiKhoanService.create_account(nhanVien.getId(), nhanVien.getMssv(), nhanVien.getSdt());
        }
        return "redirect:/nhanvien";
    }

    @PostMapping("/nhanvien/save")
    public String save(@ModelAttribute("nhanvien") NhanVien nhanVien, Model model)
    {
        Optional<NhanVien> op_nv = nhanVienService.findAll().stream().filter(
                item -> (
                        item.getMssv().equals(nhanVien.getMssv())
                        &&
                                item.getId() != nhanVien.getId()
                )).findFirst();
        if(op_nv.isPresent())
        {
            model.addAttribute("status_save", "Mã số sinh viên đã tồn tại");
            return "ui/staff/taonhanvien";
        }
        NhanVien nv = nhanVienService.findById(nhanVien.getId());
        String old_mssv = nv.getMssv();
        System.err.println(old_mssv);
        nv = nhanVienService.save(nhanVien);
        if(nv != null){
            TaiKhoan taiKhoan = taiKhoanService.findById(nv.getId());
            List<Quyen> quyens = taiKhoan.getQuyen();
            taiKhoan.setUsername(nhanVien.getMssv());
            taiKhoan = taiKhoanService.save(taiKhoan);
            if(taiKhoan != null){
                taiKhoan.setQuyen(quyens);
                session.updateUserDetails(taiKhoan, old_mssv);
            }
        }
        return "redirect:/nhanvien";
    }

    @GetMapping("/nhanvien/delete")
    public String delete(int id){
        nhanVienService.deleteById(id);
        return "redirect:/nhanvien";
    }

}
