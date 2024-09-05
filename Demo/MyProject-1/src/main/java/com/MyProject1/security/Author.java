package com.MyProject1.security;


import com.MyProject1.repositories.TaiKhoanRepository;
import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class Author {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;


    @Bean("inMemoryUserDetailsManager")
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        try{

            InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
            List<TaiKhoan> taiKhoans = taiKhoanRepository.findAll();
            taiKhoans.forEach(item -> {
                List<GrantedAuthority> authorities = new ArrayList<>();
                List<Quyen> quyens = item.getQuyen();
//                System.out.println(item.getNhanvien().getTen());
                if(quyens == null || quyens.size() == 0){
                    authorities.add(
                            new SimpleGrantedAuthority("ROLE_USER")
                    );
                }
                else{
                    for(Quyen q : quyens){
                        authorities.add(
                                new SimpleGrantedAuthority("ROLE_"+q.getTen().toUpperCase())
                        );
                    }
                }
                UserDetails user = new User(item.getUsername(), "{noop}" + item.getPassword(), authorities);
                detailsManager.createUser(user);
            });
            return detailsManager;
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        try{
            http.authorizeHttpRequests(config -> {
                config

//                        ADMIN REST SITE
                        .requestMatchers(HttpMethod.PUT,"/load/api/grant/roles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/load/api/roles/is/manager").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/load/api/account/exists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/load/api/account/username").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/load/api/account/change/password").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/load/api/account/constraint/username").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/load/api/account/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/load/api/admin/duyet/phieu/nhap").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/load/api/admin/huy/phieu/nhap").hasRole("ADMIN")

//                        USER REST SITE
                        .requestMatchers("/load/api/phieunhap/nhacungcap/search").hasRole("USER")
                        .requestMatchers("/load/api/lop/search").hasRole("USER")
                        .requestMatchers("/load/api/book/search").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/load/api/phieuxuat/create").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/phieunhap/thang").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/phieunhap/nam").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/phieuxuat/thang").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/phieuxuat/nam").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/nhapxuat/nam").hasRole("STUDENT")
                        .requestMatchers("/load/api/thongke/nhapxuat/thang").hasRole("STUDENT")

//                        MANAGER REST SITE
                        .requestMatchers("/load/api/roles/current/admin").hasRole("MANAGER")
                        .requestMatchers("/load/api/roles/current/manager").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/load/api/phieunhap/create").hasRole("MANAGER")

//                        ADMIN SITE
                        .requestMatchers("/nhacungcap/delete/**").hasRole("ADMIN")
                        .requestMatchers("/nhanvien/delete/**").hasRole("ADMIN")
                        .requestMatchers("/book/delete/**").hasRole("ADMIN")
                        .requestMatchers("/lophoc/delete/**").hasRole("ADMIN")
                        .requestMatchers("/nhanvien/create").hasRole("ADMIN")
                        .requestMatchers("/nhanvien/update").hasRole("ADMIN")
                        .requestMatchers("/nhanvien").hasRole("ADMIN")
                        .requestMatchers( "/lophoc/delete/**").hasRole("ADMIN")

//                        MANAGER site
                        .requestMatchers("/phieunhap/details/**").hasRole("MANAGER")
                        .requestMatchers("/phieuxuat/details/**").hasRole("MANAGER")
                        .requestMatchers("/phieunhap").hasRole("MANAGER")
                        .requestMatchers("/phieuxuat").hasRole("MANAGER")
                        .requestMatchers("/nhacungcap").hasRole("MANAGER")
                        .requestMatchers("/nhacungcap/create").hasRole("MANAGER")
                        .requestMatchers("/nhacungcap/update").hasRole("MANAGER")
                        .requestMatchers("/book").hasRole("MANAGER")
                        .requestMatchers( "/book/detail").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/book/create").hasRole("MANAGER")
                        .requestMatchers( "/book/create").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/book/update").hasRole("MANAGER")
                        .requestMatchers( "/book/update").hasRole("MANAGER")
                        .requestMatchers( "/lophoc").hasRole("MANAGER")
                        .requestMatchers( "/lophoc/update").hasRole("MANAGER")
                        .requestMatchers( "/lophoc/create").hasRole("MANAGER")
                        .requestMatchers("/phieunhap/create").hasRole("MANAGER")
                        .requestMatchers("/lichsu/nhap").hasRole("MANAGER")

//                        USER site
//                        .requestMatchers("/profile").hasRole("USER")
//                        .requestMatchers("/profile/change-information").hasRole("USER")
                        .requestMatchers("/lichsu/xuat/details/**").hasRole("STUDENT")
                        .requestMatchers("/lichsu/xuat").hasRole("STUDENT")
                        .requestMatchers("/phieu/thongke").hasRole("STUDENT")
                        .requestMatchers("/phieuxuat/create").hasRole("USER")
                        .requestMatchers("/", "/home", "/pictures/book/**", "/authenticated", "/logouted").hasRole("USER")
                        .requestMatchers("/**", "/pictures/main/**","/css/**", "/js/**", "/error").permitAll()
                        .anyRequest().authenticated();
            }).formLogin(form -> {
                form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .successHandler(new Authen())
                        .permitAll();
            }).logout(log -> {
                log.logoutSuccessHandler(new Authen());
            }).exceptionHandling(handle -> {
                handle.accessDeniedPage("/error");
            }).httpBasic(Customizer.withDefaults());

            http.csrf(csfr -> csfr.disable());

            return http.build();
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}
