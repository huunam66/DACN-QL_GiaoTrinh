package com.MyProject1.service;

import com.MyProject1.entity.ChiTietPhieuNhap;
import com.MyProject1.entity.PhieuNhap;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

public interface PhieuNhapService {
    List<PhieuNhap> findAll();
    void save(PhieuNhap phieuNhap);
    void deleteById(int id);

    Boolean tao_phieu(ModelMap model);

    PhieuNhap get_phieu_nhap(int id);

    List<PhieuNhap> thongke_phieunhap_theothang(int year, int month);
    List<PhieuNhap> thongke_phieunhap_theonam(int year);

    int tongSoGiaoTrinh(int id);

    Boolean duyetPhieu(ModelMap model);
    Boolean huyPhieu(ModelMap model);
}
