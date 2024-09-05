package com.MyProject1.service;

import com.MyProject1.entity.GiaoTrinh;

import java.util.List;

public interface GiaoTrinhService {
    List<GiaoTrinh> findAll();
    GiaoTrinh findById(int id);
    GiaoTrinh save(GiaoTrinh giaoTrinh);
    void deleteById(int id);

    List<GiaoTrinh> search(String name);

    List<GiaoTrinh> thongke_tonkho();
    List<GiaoTrinh> thongke_tonkho(long giaotrinh_id);
}
