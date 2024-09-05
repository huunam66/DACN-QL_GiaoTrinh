package com.MyProject1.service;

import com.MyProject1.entity.Lop;

import java.util.List;

public interface LopService {
    List<Lop> search(String search);

    Lop findById(int id);

    List<Lop> findAll();

    void deleteById(int id);
    Lop save(Lop lop);

    Boolean isExistsLop(Lop lop);
}
