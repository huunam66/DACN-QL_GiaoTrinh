package com.MyProject1.service.Excel;

import com.MyProject1.entity.Lop;
import com.MyProject1.service.LopService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImple implements ExcelService{

    @Autowired
    private LopService lopService;
    @Override
    public List<Lop> readExcel_Lop(MultipartFile file) {
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet("Lop");
            List<Lop> lops = new ArrayList<>();
            Boolean title_line = true;
            for(Row row : sheet){
                if(title_line){
                    title_line = false;
                    continue;
                }
                Lop lop = new Lop();
                lop.setTen(row.getCell(1).getStringCellValue());
                lop.setMonhoc(row.getCell(2).getStringCellValue());
                lop.setGiangvien(row.getCell(3).getStringCellValue());
                lop.setPhonghoc(row.getCell(4).getStringCellValue());
                lop.setThoigianhoc(row.getCell(5).getStringCellValue());
                lop.setNamhoc(row.getCell(6).getStringCellValue());
                lops.add(lop);
            }
            return lops;
        }
        catch (IOException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    public void inject_data_Lop_to_DB(List<Lop> lops) {
        try{
            if(lops != null && lops.size() > 0) {
                for (Lop lop : lops) {
                    if (lopService.isExistsLop(lop))
                        continue;

                    lopService.save(lop);
                }
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
    }
}
