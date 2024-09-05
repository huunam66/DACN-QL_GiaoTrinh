package com.MyProject1.repositories;

import com.MyProject1.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    NhanVien findByMssv(String mssv);
}
