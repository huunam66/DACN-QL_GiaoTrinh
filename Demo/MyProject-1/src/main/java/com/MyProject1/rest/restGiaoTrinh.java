package com.MyProject1.rest;

import com.MyProject1.entity.GiaoTrinh;
import com.MyProject1.service.GiaoTrinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping("/load/api")
public class restGiaoTrinh {

    @Autowired
    private GiaoTrinhService giaoTrinhService;

    @RequestMapping(value = "/book/search", method = RequestMethod.GET)
    public List<Map<String, Object>> book_read_all(@RequestParam(name = "") String name){
        List<Map<String, Object>> mapList = new ArrayList<>();

        List<GiaoTrinh> giaoTrinhList = giaoTrinhService.search(name);
        System.err.println(giaoTrinhList.size());
        if(giaoTrinhList != null && giaoTrinhList.size() > 0){
            giaoTrinhList.forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("ten", item.getTen());
                Map<String, Object> chuyennganh = new HashMap<>();
                chuyennganh.put("id", item.getChuyennganh().getId());
                chuyennganh.put("tenchuyennganh", item.getChuyennganh().getTenchuyennganh());
                map.put("chuyennganh", chuyennganh);
                map.put("tacgia", item.getTacgia());
                map.put("nhaxuatban", item.getNhaxuatban());
                map.put("ngaytao", new SimpleDateFormat("yyyy-MM-dd").format(item.getNgaytao()));
                map.put("gia", item.getGia());
                map.put("soluong", item.getSoluong());
                map.put("hinhanh", item.getHinhanh());
                mapList.add(map);
            });
        }
        return mapList;
    }
}
