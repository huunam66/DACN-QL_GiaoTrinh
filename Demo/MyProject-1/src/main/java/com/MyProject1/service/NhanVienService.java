package com.MyProject1.service;

import com.MyProject1.entity.NhanVien;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> List_NhanVien();

    List<NhanVien> findAll();
    NhanVien findById(int id);
    NhanVien save(NhanVien nhanVien);
    void deleteById(int id);

    NhanVien findByMssv(String mssv);

}
