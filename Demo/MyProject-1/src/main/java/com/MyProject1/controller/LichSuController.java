package com.MyProject1.controller;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.config.support.Maker;
import com.MyProject1.entity.Lop;
import com.MyProject1.entity.NhanVien;
import com.MyProject1.entity.PhieuNhap;
import com.MyProject1.entity.PhieuXuat;
import com.MyProject1.service.PhieuNhapService;
import com.MyProject1.service.PhieuXuatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class LichSuController {

    private final String group = "phieu";

    private final int limit_item = 5;

    private final PhieuNhapService phieuNhapService;
    private final PhieuXuatService phieuXuatService;


    @Autowired
    public LichSuController(PhieuNhapService phieuNhapService, PhieuXuatService phieuXuatService) {
        this.phieuNhapService = phieuNhapService;
        this.phieuXuatService = phieuXuatService;
    }

    @Autowired
    private SessionManager session;

    @GetMapping("/lichsu/nhap")
    public String lich_su_nhap(Model model,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "sort", defaultValue = "") String sort,
                            @RequestParam(name = "search", defaultValue = "") String search,
                            @RequestParam(name = "type", defaultValue = "") String type){

        Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        System.err.println(role);
        model.addAttribute("role", role);

        model.addAttribute("group", group);
        session.setAttribute("current_page", "/lichsu/nhap");
        model.addAttribute("fullname", session.get_session_fullname_token());

        model.addAttribute("title", "Lịch sử nhập phiếu");
        System.err.println(session.get_session_id_token());
        List<PhieuNhap> phieuNhaps = phieuNhapService.findAll().stream().filter(item -> item.getNhanvien() != null && item.getNhanvien().getId() == session.get_session_id_token()).toList();
        phieuNhaps = phieuNhaps.stream().sorted(Comparator.comparing(PhieuNhap::getId).reversed()).toList();
        phieuNhaps = phieuNhaps.stream().filter(item -> (
                item.getNhacungcap() != null ? item.getNhacungcap().getTen().toLowerCase().contains(search.toLowerCase()) : false
                        ||
                        item.getNhanvien() != null ? item.getNhanvien().getTen().toLowerCase().contains(search.toLowerCase()) : false
                        ||
                        item.getNhanvien() != null ? item.getNhanvien().getMssv().toLowerCase().contains(search.toLowerCase()) : false
        )).toList();
        model.addAttribute("search", search);

        int all_phieu_size = phieuNhaps.size();
        int phieu_size = 0;

        if(type.equals("")){
            phieu_size = phieuNhaps.stream().filter(item -> item.getTinhtrang().toLowerCase().equals("chưa duyệt")).toList().size();
        }
        else{
            phieuNhaps = phieuNhaps.stream().filter(item -> item.getTinhtrang().toLowerCase().equals(type.toLowerCase())).toList();
            phieu_size = phieuNhaps.size();
            System.err.println(phieu_size);
        }
        System.err.println(type);
        model.addAttribute("all_phieu_size", all_phieu_size);
        model.addAttribute("phieu_size", phieu_size);

        if(sort.equals("asc")){
            phieuNhaps = phieuNhaps.stream().sorted(Comparator.comparing(PhieuNhap::getTongtien)).toList();

        }
        if(sort.equals("desc")){
            phieuNhaps = phieuNhaps.stream().sorted(Comparator.comparing(PhieuNhap::getTongtien).reversed()).toList();

        }
        int max_size = phieuNhaps.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);

        phieuNhaps = phieuNhaps.stream().skip((page - 1) * 5).limit(limit_item).toList();

        int len = phieuNhaps.size();
        int[] tongsoluong = new int[len];
        for(int i = 0; i < len; i++){
            int count = phieuNhapService.tongSoGiaoTrinh(phieuNhaps.get(i).getId());
            tongsoluong[i] = count;
        }
        model.addAttribute("tongsoluong", tongsoluong);

        model.addAttribute("count_item", phieuNhaps.size());
        model.addAttribute("sort", sort);
        model.addAttribute("type", type);
        model.addAttribute("phieuNhaps", phieuNhaps);
        return "ui/staff/lichsu-nhap";
    }
    @GetMapping("/lichsu/xuat")
    public String phieuxuat(Model model,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "search", defaultValue = "") String search,
                            @RequestParam(name = "sort", defaultValue = "") String sort){


        Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        System.err.println(role);
        model.addAttribute("role", role);


        model.addAttribute("group", group);
        session.setAttribute("current_page", "/lichsu/xuat");
        model.addAttribute("fullname", session.get_session_fullname_token());


        model.addAttribute("title", "Lịch sử xuất phiếu");

        List<PhieuXuat> phieuXuats = phieuXuatService.findAll().stream().filter(item -> item.getNhanvien() != null && item.getNhanvien().getId() == session.get_session_id_token()).toList();
        phieuXuats = phieuXuats.stream().sorted(Comparator.comparing(PhieuXuat::getId).reversed()).toList();

        phieuXuats = phieuXuats.stream().filter(item -> (
                item.getLop() != null && item.getLop().getTen().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getLop() != null && item.getLop().getMonhoc().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getLop() != null && item.getLop().getPhonghoc().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getLop() != null && item.getLop().getGiangvien().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getNhanvien() != null && item.getNhanvien().getMssv().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getNhanvien() != null && item.getTennguoinhan().toLowerCase().contains(search.toLowerCase())
                        ||
                        item.getNhanvien() != null && item.getSdtnguoinhan().toLowerCase().contains(search.toLowerCase())
        )).toList();
        model.addAttribute("search", search);
        if(sort.equals("asc")){
            phieuXuats = phieuXuatService.findAll().stream().sorted(Comparator.comparing(PhieuXuat::getTongtien)).toList();

        }
        if(sort.equals("desc")){
            phieuXuats = phieuXuatService.findAll().stream().sorted(Comparator.comparing(PhieuXuat::getTongtien).reversed()).toList();

        }

        int max_size = phieuXuats.size();
        model.addAttribute("max_size", max_size);

        model.addAttribute("count_page", Maker.calc_count_page(max_size, limit_item));
        model.addAttribute("page", page);

        phieuXuats = phieuXuats.stream().skip((page - 1) * 5).limit(limit_item).toList();

        int len = phieuXuats.size();
        int[] tongsoluong = new int[len];
        for(int i = 0; i < len; i++){
            int count = phieuXuatService.tongSoGiaoTrinh(phieuXuats.get(i).getId());
            tongsoluong[i] = count;
        }
        model.addAttribute("tongsoluong", tongsoluong);


        model.addAttribute("count_item", phieuXuats.size());
        model.addAttribute("sort", sort);

        model.addAttribute("phieuXuats", phieuXuats);
        return "ui/staff/lichsu-xuat";
    }

    @RequestMapping(value = "/lichsu/xuat/details/{id}", method = RequestMethod.GET)
    public String phieuxuat_details(Model model, @PathVariable("id") int id){

        Boolean admin = session.admin(true);
        Boolean manager = session.manager(true);
        Boolean user = session.user(true);

        String role = admin ? "ADMIN" : manager ? "MANAGER" : user ? "USER" : "USER";
        System.err.println(role);
        model.addAttribute("role", role);


        model.addAttribute("group", group);
        model.addAttribute("create_nhap_xuat", true);
        session.setAttribute("current_page", "/phieunhap/details/" + id);
        model.addAttribute("fullname", session.get_session_fullname_token());
        model.addAttribute("id", session.get_session_id_token());


        model.addAttribute("title", "Chi tiết phiếu xuất");

        PhieuXuat phieuXuat = phieuXuatService.get_phieu_xuat(id);
        if(phieuXuat == null){
            phieuXuat = new PhieuXuat();
            phieuXuat.setChitietphieuxuats(new ArrayList<>());
            phieuXuat.setNhanvien(new NhanVien());
            phieuXuat.setLop(new Lop());
            model.addAttribute("phieunhap", phieuXuat);
            return "ui/phieu/phieunhapdetails";
        }

        model.addAttribute("phieuxuat", phieuXuat);
        int tongsoluong = phieuXuat.getChitietphieuxuats().stream().mapToInt(item -> item.getSoluong()).sum();
        model.addAttribute("tongsoluong", tongsoluong);
        return "ui/phieu/phieuxuatdetails";
    }

}
