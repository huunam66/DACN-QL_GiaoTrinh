package com.MyProject1.rest;

import com.MyProject1.entity.Lop;
import com.MyProject1.service.Excel.ExcelService;
import com.MyProject1.service.Excel.ExcelServiceImple;
import com.MyProject1.service.LopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restLop {

    @Autowired
    private LopService lopService;

    @Autowired
    private ExcelService excelService;

    @RequestMapping(value = "/lop/search", method = RequestMethod.GET)
    public List<Map<String, Object>> search_list(
            @RequestParam("text") String text
    ){
        List<Lop> list_lop = lopService.search(text);
        List<Map<String, Object>> map_list = new ArrayList<>();
        list_lop.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("tenlop", item.getTen());
            map.put("monhoc", item.getMonhoc());
            map.put("giangvien", item.getGiangvien());
            map.put("phonghoc", item.getPhonghoc());
            map.put("thoigianhoc", item.getThoigianhoc());
            map.put("namhoc", item.getNamhoc());
            map_list.add(map);
        });
        return map_list;
    }

    @RequestMapping(value = "/lop/excel/read", method = RequestMethod.POST)
    public Map<String, Object> read_excel(
            @RequestParam("file") MultipartFile file
    ){
        Map<String, Object> map = new HashMap();
        try{
            if(ExcelService.isValidExcelFile(file)){
                excelService.inject_data_Lop_to_DB(
                        excelService.readExcel_Lop(file)
                );
                map.put("status", "isValid");
                map.put("content", "Đọc file EXCEL thành công !");
                return map;
            }
            map.put("status", "inValid");
            map.put("content", "Không đúng định dạng file !");
        }
        catch (RestClientException e){
            System.err.println(e);
            map.put("status", "error");
            map.put("content", "Xảy ra lỗi !");
            map.put("error", e.getMessage());
        }
        return map;
    }
}
