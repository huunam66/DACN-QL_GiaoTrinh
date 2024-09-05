package com.MyProject1.service;

import com.MyProject1.repositories.ChuyenNganhRepository;
import com.MyProject1.entity.ChuyenNganh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChuyenNganhServiceImple implements ChuyenNganhService{

    @Autowired
    private ChuyenNganhRepository chuyenNganhRepository;
    @Override
    public List<ChuyenNganh> List_ChuyenNganh() {
        try{
            return chuyenNganhRepository.findAll();
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}
