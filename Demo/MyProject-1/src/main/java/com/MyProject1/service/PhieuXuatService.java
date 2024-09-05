package com.MyProject1.service;

import com.MyProject1.entity.GiaoTrinh;
import com.MyProject1.entity.PhieuNhap;
import com.MyProject1.entity.PhieuXuat;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface PhieuXuatService {
    List<PhieuXuat> findAll();
    void save(PhieuXuat phieuXuat);
    void deleteById(int id);

    Boolean tao_phieu(ModelMap model);

    PhieuXuat get_phieu_xuat(int id);

    int tongSoGiaoTrinh(int id);
    List<PhieuXuat> thongke_phieuxuat_theothang(int year, int month);
    List<PhieuXuat> thongke_phieuxuat_theonam(int year);


    GiaoTrinh update_count_giaotrinh_tang(ModelMap model);
    GiaoTrinh update_count_giaotrinh_giam(ModelMap model);


}
