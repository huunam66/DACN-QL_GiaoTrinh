package com.MyProject1.service.Excel;

import com.MyProject1.entity.Lop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

public interface ExcelService {

    public static Boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    List<Lop> readExcel_Lop(MultipartFile file);
    void inject_data_Lop_to_DB(List<Lop> lops);
}
