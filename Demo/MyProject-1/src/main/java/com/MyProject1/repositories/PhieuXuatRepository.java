package com.MyProject1.repositories;

import com.MyProject1.entity.PhieuXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuXuatRepository extends JpaRepository<PhieuXuat, Integer> {

}
