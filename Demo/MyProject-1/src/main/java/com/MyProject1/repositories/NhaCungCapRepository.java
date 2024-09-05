package com.MyProject1.repositories;

import com.MyProject1.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {
    //find by ten like
    @Query("SELECT ncc FROM NhaCungCap ncc WHERE ncc.ten LIKE %?1%")
    List<NhaCungCap> findByTen(String ten);
}
