package com.MyProject1.rest;

import com.MyProject1.entity.PhieuNhap;
import com.MyProject1.entity.PhieuXuat;
import com.MyProject1.service.PhieuNhapService;
import com.MyProject1.service.PhieuXuatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restThongKe {
    @Autowired
    private PhieuNhapService phieuNhapService;
    @Autowired
    private PhieuXuatService phieuXuatService;

    @RequestMapping(value = "/thongke/phieunhap/thang", method = RequestMethod.GET)
    public List<Map<String, Object>> thongke_phieunhap(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        List<PhieuNhap> phieuNhapList = phieuNhapService.thongke_phieunhap_theothang(year, month);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map_value = new HashMap<>();
        map_value.put("tongphieu", phieuNhapList.size());
        map_value.put("thang", "Tháng " + month);
        Double tong_chi = phieuNhapList.stream().mapToDouble(item -> item.getTongtien()).sum();
        map_value.put("tongchi", String.format("%.2f", tong_chi));
        mapList.add(map_value);
        return mapList;
    }

    @RequestMapping(value = "/thongke/phieunhap/nam", method = RequestMethod.GET)
    public List<Map<String, Object>> thongke_phieunhap(
            @RequestParam("year") int year
    ){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        List<PhieuNhap> phieuNhaps = phieuNhapService.thongke_phieunhap_theonam(year);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            int thang = i;
            List<PhieuNhap> phieuNhaps_thang_i = phieuNhaps.stream().filter(
                    item -> Integer.parseInt(dateFormat.format(item.getNgay())) == thang
            ).toList();
            Map<String, Object> map_value = new HashMap<>();
            map_value.put("tongphieu", phieuNhaps_thang_i.size());
            map_value.put("thang", "Tháng " + i);
            Double tong_chi = phieuNhaps_thang_i.stream().mapToDouble(item -> item.getTongtien()).sum();
            map_value.put("tongchi", String.format("%.2f", tong_chi));
            mapList.add(map_value);
        }
        return mapList;
    }


    @RequestMapping(value = "/thongke/phieuxuat/thang", method = RequestMethod.GET)
    public List<Map<String, Object>> thongke_phieuxuat(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        List<PhieuXuat> phieuXuatList = phieuXuatService.thongke_phieuxuat_theothang(year, month);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map_value = new HashMap<>();
        map_value.put("tongphieu", phieuXuatList.size());
        map_value.put("thang", "Tháng " + month);
        Double tong_chi = phieuXuatList.stream().mapToDouble(item -> item.getTongtien()).sum();
        map_value.put("tongthu", String.format("%.2f", tong_chi));
        mapList.add(map_value);
        return mapList;
    }

    @RequestMapping(value = "/thongke/phieuxuat/nam", method = RequestMethod.GET)
    public List<Map<String, Object>> thongke_phieuxuat(
            @RequestParam("year") int year
    ){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        List<PhieuXuat> phieuXuats = phieuXuatService.thongke_phieuxuat_theonam(year);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            int thang = i;
            List<PhieuXuat> phieuXuats_thang_i = phieuXuats.stream().filter(
                    item -> Integer.parseInt(dateFormat.format(item.getNgay())) == thang
            ).toList();
            Map<String, Object> map_value = new HashMap<>();
            map_value.put("tongphieu", phieuXuats_thang_i.size());
            map_value.put("thang", "Tháng " + i);
            Double tong_thu = phieuXuats_thang_i.stream().mapToDouble(item -> item.getTongtien()).sum();
            map_value.put("tongthu", String.format("%.2f", tong_thu));
            mapList.add(map_value);
        }
        return mapList;
    }

    @RequestMapping(value = "/thongke/nhapxuat/nam", method = RequestMethod.GET)
    public Map<String, Object> phieu_thongke_nhapxuat(
        @RequestParam("year") int year
    ){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");

        List<PhieuNhap> phieuNhaps = phieuNhapService.thongke_phieunhap_theonam(year);
        List<PhieuXuat> phieuXuats = phieuXuatService.thongke_phieuxuat_theonam(year);

        List<Map<String, Object>> tk_nhap = new ArrayList<>();
        List<Map<String, Object>> tk_xuat = new ArrayList<>();

        for(int i = 1; i <= 12; i++){
            int thang = i;
            List<PhieuNhap> phieuNhapList = phieuNhaps.stream().filter(
                    item -> Integer.parseInt(dateFormat.format(item.getNgay())) == thang
            ).toList();
            List<PhieuXuat> phieuXuatList = phieuXuats.stream().filter(
                    item -> Integer.parseInt(dateFormat.format(item.getNgay())) == thang
            ).toList();

            Map<String, Object> mapNhap = new HashMap<>();
            mapNhap.put("thang", "Tháng " + i);
            mapNhap.put("tongphieu", phieuNhapList.size());
            Double tongChiNhap = phieuNhapList.stream().mapToDouble(item -> item.getTongtien()).sum();
            mapNhap.put("tongchi", String.format("%.2f", tongChiNhap));
            tk_nhap.add(mapNhap);

            Map<String, Object> mapXuat = new HashMap<>();
            mapXuat.put("thang", "Tháng " + i);
            mapXuat.put("tongphieu", phieuXuatList.size());
            Double tongThuXuat = phieuXuatList.stream().mapToDouble(item -> item.getTongtien()).sum();
            mapXuat.put("tongthu", String.format("%.2f", tongThuXuat));
            tk_xuat.add(mapXuat);
        }

        Map<String, Object> respone = new HashMap<>();
        respone.put("phieunhap", tk_nhap);
        respone.put("phieuxuat", tk_xuat);

        return respone;
    }

    @RequestMapping(value = "/thongke/nhapxuat/thang", method = RequestMethod.GET)
    public Map<String, Object> phieu_thongke_nhapxuat(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        List<PhieuNhap> phieuNhaps = phieuNhapService.thongke_phieunhap_theothang(year, month);
        List<PhieuXuat> phieuXuats = phieuXuatService.thongke_phieuxuat_theothang(year, month);

        List<Map<String, Object>> tk_nhap = new ArrayList<>();
        List<Map<String, Object>> tk_xuat = new ArrayList<>();

        Map<String, Object> mapNhap = new HashMap<>();

        mapNhap.put("thang", "Tháng " + month);
        mapNhap.put("tongphieu", phieuNhaps.size());
        Double tongChiNhap = phieuNhaps.stream().mapToDouble(item -> item.getTongtien()).sum();
        mapNhap.put("tongchi", String.format("%.2f", tongChiNhap));
        tk_nhap.add(mapNhap);

        Map<String, Object> mapXuat = new HashMap<>();
        mapXuat.put("thang", "Tháng " + month);
        mapXuat.put("tongphieu", phieuXuats.size());
        Double tongThuXuat = phieuXuats.stream().mapToDouble(item -> item.getTongtien()).sum();
        mapXuat.put("tongthu", String.format("%.2f", tongThuXuat));
        tk_xuat.add(mapXuat);

        Map<String, Object> respone = new HashMap<>();
        respone.put("phieunhap", tk_nhap);
        respone.put("phieuxuat", tk_xuat);

        return respone;
    }
}
