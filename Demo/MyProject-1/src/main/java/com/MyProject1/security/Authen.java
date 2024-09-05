package com.MyProject1.security;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.TaiKhoan;
import com.MyProject1.repositories.TaiKhoanRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Configuration
public class Authen implements AuthenticationSuccessHandler, LogoutSuccessHandler {


//    NDTE: không thể goi các bean ở tỏng này

//    @Autowired
//    @Qualifier("session_imple")
//    private SessionManager sessionManager;
//    -> việc Autowired để inject là không khả thi !


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
//        HttpSession session = request.getSession();
//
//        Object current_page = session.getAttribute("current_page");
//        String redirect_url = current_page != null ? current_page.toString() : "/";

        response.sendRedirect("/authenticated");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

//        HttpSession session = request.getSession();
//        Object current_page = session.getAttribute("current_page");
//        String redirect_url = current_page != null ? current_page.toString() : "/";

        response.sendRedirect("/logouted");
    }
}
