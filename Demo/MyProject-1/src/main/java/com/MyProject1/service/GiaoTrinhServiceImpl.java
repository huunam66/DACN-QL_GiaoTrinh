package com.MyProject1.service;

import com.MyProject1.repositories.GiaoTrinhRepository;
import com.MyProject1.entity.GiaoTrinh;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiaoTrinhServiceImpl implements GiaoTrinhService {
    @Autowired
    private final GiaoTrinhRepository giaoTrinhRepository;

    @Autowired
    public GiaoTrinhServiceImpl(GiaoTrinhRepository giaoTrinhRepository) {
        this.giaoTrinhRepository = giaoTrinhRepository;
    }

    @Override
    public List<GiaoTrinh> findAll() {
        return giaoTrinhRepository.findAll();
    }

    @Override
    public GiaoTrinh findById(int id) {
        Optional<GiaoTrinh> result = giaoTrinhRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    @Override
    public GiaoTrinh save(GiaoTrinh giaoTrinh) {
        return giaoTrinhRepository.save(giaoTrinh);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        giaoTrinhRepository.deleteById(id);
    }


    @Override
    public List<GiaoTrinh> search(String name) {
        List<GiaoTrinh> giaoTrinhList = giaoTrinhRepository.findAll().stream().filter(
            item -> (
                item.getTen().toLowerCase().contains(name.toLowerCase())
                ||
                item.getChuyennganh().getTenchuyennganh().contains(name.toLowerCase())
            )
        ).toList();
        return giaoTrinhList;
    }

    @Override
    public List<GiaoTrinh> thongke_tonkho() {
        return null;
    }

    @Override
    public List<GiaoTrinh> thongke_tonkho(long giaotrinh_id) {
        return null;
    }
}
