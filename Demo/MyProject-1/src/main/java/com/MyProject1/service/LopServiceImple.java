package com.MyProject1.service;

import com.MyProject1.entity.Lop;
import com.MyProject1.repositories.LopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LopServiceImple implements LopService{

    @Autowired
    private LopRepository lopRepository;

    @Override
    public List<Lop> search(String search) {
        return lopRepository.findAll().stream().filter(
                item ->(
                        item.getTen().contains(search)
                        ||
                                item.getGiangvien().contains(search)
                        ||
                                item.getMonhoc().contains(search)
                        ||
                                item.getPhonghoc().contains(search)
                        )).toList();
    }

    @Override
    public Lop findById(int id) {
        return lopRepository.findById(id).get();
    }

    @Override
    public Lop save(Lop lop) {
        return lopRepository.save(lop);
    }

    @Override
    public void deleteById(int id) {
        lopRepository.deleteById(id);
    }

    @Override
    public List<Lop> findAll() {
        return lopRepository.findAll();
    }

    @Override
    public Boolean isExistsLop(Lop lop) {
        return lopRepository.findAll().stream().filter(
                item -> (
                        item.getTen().equals(lop.getTen())
                        && item.getPhonghoc().equals(lop.getPhonghoc())
                        && item.getThoigianhoc().equals(lop.getThoigianhoc())
                        && item.getNamhoc().equals(lop.getNamhoc())
                        )
        ).findFirst().isPresent();
    }
}
