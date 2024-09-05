package com.MyProject1.rest;

import com.MyProject1.entity.*;
import com.MyProject1.service.NhaCungCapService;
import com.MyProject1.service.PhieuNhapService;
import com.MyProject1.service.PhieuXuatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restNhapXuat {

    @Autowired
    private PhieuNhapService phieuNhapService;

    @Autowired
    private PhieuXuatService phieuXuatService;


    @RequestMapping(value = "/create/phieuxuat/soluong/change/giaotrinh/tonkho/tang", method = RequestMethod.PUT)
    public Map<String, Object> giaotrinh_tonkho_tang(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            GiaoTrinh gt = phieuXuatService.update_count_giaotrinh_tang(model);
            if(gt != null){
                map.put("status", true);
                map.put("tonkho", gt.getSoluong());
                return map;
            }
        }
        catch (Exception e){
            map.put("error", e.getStackTrace());
        }
        map.put("status", false);
        return map;
    }

    @RequestMapping(value = "/create/phieuxuat/soluong/change/giaotrinh/tonkho/giam", method = RequestMethod.PUT)
    public Map<String, Object> giaotrinh_tonkho_giam(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            GiaoTrinh gt = phieuXuatService.update_count_giaotrinh_giam(model);
            if(gt != null){
                map.put("status", true);
                map.put("tonkho", gt.getSoluong());
                return map;
            }
        }
        catch (Exception e){
            map.put("error", e.getStackTrace());
        }
        map.put("status", false);
        return map;
    }


    @RequestMapping(value = "/phieunhap/create", method = RequestMethod.POST)
    public Map<String, Object> phieunhap_create(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            System.err.println("hello");
            Boolean nhap = phieuNhapService.tao_phieu(model);
            if(nhap){
                map.put("status", "Tạo phiếu nhập thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        }
        catch (Exception e){
            map.put("message", e.getMessage());
        }
        map.put("status", "Tạo phiếu nhập thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");
        return map;
    }

    @RequestMapping(value = "/phieuxuat/create", method = RequestMethod.POST)
    public Map<String, Object> phieuxuat_create(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            Boolean xuat = phieuXuatService.tao_phieu(model);
            if(xuat){
                map.put("status", "Tạo phiếu xuất thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        }
        catch (Exception e){
            map.put("message", e.getMessage());
        }
        map.put("status", "Tạo phiếu xuất thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");
        return map;
    }

    @RequestMapping(value = "/admin/duyet/phieu/nhap", method = RequestMethod.PUT)
    public Map<String, Object> duyet_phieu_nhap(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            Boolean duyet = phieuNhapService.duyetPhieu(model);
            if(duyet){
                map.put("status", "Duyệt phiếu thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        }
        catch (Exception e){
            map.put("message", e.getMessage());
        }
        map.put("status", "Duyệt phiếu thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");
        return map;
    }

    @RequestMapping(value = "/admin/huy/phieu/nhap", method = RequestMethod.PUT)
    public Map<String, Object> huy_phieu_nhap(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            Boolean duyet = phieuNhapService.huyPhieu(model);
            if(duyet){
                map.put("status", "Hủy phiếu thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        }
        catch (Exception e){
            map.put("message", e.getMessage());
        }
        map.put("status", "Hủy phiếu thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");
        return map;
    }

}
