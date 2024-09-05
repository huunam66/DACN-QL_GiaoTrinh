package com.MyProject1.service;

import com.MyProject1.repositories.NhanVienRepository;
import com.MyProject1.entity.NhanVien;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhanVienServiceImple implements NhanVienService{


    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Override
    public List<NhanVien> List_NhanVien() {
        try{

            List<NhanVien> nhanViens = nhanVienRepository.findAll();
            return nhanViens;
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    public List<NhanVien> findAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public NhanVien findById(int id) {
        Optional<NhanVien> result = nhanVienRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    @Override
    public NhanVien save(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        nhanVienRepository.deleteById(id);
    }

    @Override
    public NhanVien findByMssv(String mssv) {
        return nhanVienRepository.findByMssv(mssv);
    }
}
