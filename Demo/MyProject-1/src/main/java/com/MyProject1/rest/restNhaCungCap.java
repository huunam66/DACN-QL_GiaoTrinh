package com.MyProject1.rest;

import com.MyProject1.entity.NhaCungCap;
import com.MyProject1.service.NhaCungCapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restNhaCungCap {

    @Autowired
    NhaCungCapService nhaCungCapService;
    @RequestMapping(value = "/phieunhap/nhacungcap/search", method = RequestMethod.GET)
    public List<Map<String, Object>> phieunhap_nhacungcap_search(
            @RequestParam(name="name", defaultValue = "") String search){
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<NhaCungCap> nccs = nhaCungCapService.search(search);
        if(nccs != null && nccs.size() > 0){
            nccs.forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("ten", item.getTen());
                map.put("diachi", item.getDiachi());
                map.put("phuongxa", item.getPhuongxa());
                map.put("quanhuyen", item.getQuanhuyen());
                map.put("tinhthanh", item.getTinhthanh());
                map.put("sdt", item.getSdt());
                map.put("email", item.getEmail());
                mapList.add(map);
            });
        }
        return mapList;
    }
}
