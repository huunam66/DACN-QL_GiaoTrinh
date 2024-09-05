package com.MyProject1.service;

import com.MyProject1.repositories.NhaCungCapRepository;
import com.MyProject1.entity.NhaCungCap;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private final NhaCungCapRepository nhacungcapRepository;

    @Autowired
    public NhaCungCapServiceImpl(NhaCungCapRepository nhacungcapRepository) {
        this.nhacungcapRepository = nhacungcapRepository;
    }

    @Override
    public List<NhaCungCap> findAll() {
        return nhacungcapRepository.findAll();
    }

    @Override
    public NhaCungCap findById(int id) {
        Optional<NhaCungCap> ncc = nhacungcapRepository.findById(id);
        return ncc.orElse(null);
    }

    @Transactional
    @Override
    public void save(NhaCungCap nhaCungCap) {
        nhacungcapRepository.save(nhaCungCap);

    }

    @Transactional
    @Override
    public void deleteById(int id) {
        nhacungcapRepository.deleteById(id);
    }

    @Override
    public List<NhaCungCap> findByTen(String ten) {
        return nhacungcapRepository.findByTen(ten);
    }

    @Override
    public List<NhaCungCap> search(String name) {
        List<NhaCungCap> result = nhacungcapRepository.findAll().stream().filter(
            item -> (
                item.getTen().toLowerCase().contains(name.toLowerCase())
                    ||
                item.getSdt().toLowerCase().contains(name.toLowerCase())
            )
        ).toList();
        return result;
    }
}
