package com.MyProject1.service;

import com.MyProject1.entity.NhaCungCap;

import java.util.List;

public interface NhaCungCapService {
    List<NhaCungCap> findAll();
    NhaCungCap findById(int id);
    void save(NhaCungCap nhaCungCap);
    void deleteById(int id);

    List<NhaCungCap> findByTen(String ten);

    List<NhaCungCap> search(String name);

}
