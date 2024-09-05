package com.MyProject1.config.support;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface Maker {

    public static String PATH_SOURCE(){
        return new File("").getAbsolutePath();
    }

    public static Boolean upload_file(String folder, MultipartFile file){
        try{
            String folder_file = Maker.PATH_SOURCE() + "\\src\\main\\upload\\static\\pictures";
            File f = new File(folder_file);
            if(!f.exists()) f.mkdirs();
            folder_file = f.getAbsolutePath() + folder;
            f = new File(folder_file);
            if(!f.exists()) f.mkdirs();
            f = new File(f.getAbsolutePath() + "\\" + file.getOriginalFilename());
            System.err.println(f.getAbsolutePath());
            if(!f.exists()) file.transferTo(f);
            return true;
        }
        catch (Exception e){
            System.err.println(e);
        }
        return false;
    }

    public static float calc_count_page(int size_list, int limit){
        float calc_result = (float)size_list / limit;
        if(calc_result > (int)calc_result)
            return (int)calc_result + 1;
        return calc_result;
    }

    public static List<File> read_all_file(String folder){
        String folder_file = Maker.PATH_SOURCE() + "\\src\\main\\resources\\static"+folder+"\\";
        File[] fs = new File(folder_file).listFiles();
        try{
            return Arrays.stream(fs).toList();
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}
