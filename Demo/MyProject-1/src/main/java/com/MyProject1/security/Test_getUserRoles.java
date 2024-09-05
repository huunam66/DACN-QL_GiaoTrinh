package com.MyProject1.security;


import com.MyProject1.entity.TaiKhoan;
import com.MyProject1.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Test_getUserRoles {

    @Autowired
    @Qualifier("TaiKhoanServiceImple")
    private TaiKhoanService taiKhoanService;

    @Bean("testcallrole")
    public void test(){
        List<TaiKhoan> taiKhoans = taiKhoanService.List_TaiKhoan();
        taiKhoans.forEach(item -> {
            System.out.println(item.getUsername());
            System.out.println("Quyen:");
            item.getQuyen().forEach(quyen -> System.out.println(quyen.getTen()));
        });
    }
}
