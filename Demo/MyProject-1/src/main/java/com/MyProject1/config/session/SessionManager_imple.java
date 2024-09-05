package com.MyProject1.config.session;

import com.MyProject1.repositories.TaiKhoanRepository;
import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration("session_imple")
public class SessionManager_imple implements SessionManager {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    @Qualifier("inMemoryUserDetailsManager")
    private UserDetailsManager userDetailsManager;

    @Override
    public Boolean addUserDetails(TaiKhoan taiKhoan) {
        try {
            Boolean user_exists = userDetailsManager.userExists(taiKhoan.getUsername());
            if(user_exists) userDetailsManager.deleteUser(taiKhoan.getUsername());

            List<GrantedAuthority> authorities = new ArrayList<>();
            taiKhoan.getQuyen().forEach(item -> {
                System.out.println(item.getTen());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getTen().toUpperCase()));
            });

            UserDetails userDetails = new User(taiKhoan.getUsername(), "{noop}" + taiKhoan.getPassword(), authorities);
            userDetailsManager.createUser(userDetails);
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Boolean updateUserDetails(TaiKhoan taiKhoan, String old_Username) {
        try {
            Boolean user_exists = userDetailsManager.userExists(old_Username);
            if(user_exists) userDetailsManager.deleteUser(old_Username);

            List<GrantedAuthority> authorities = new ArrayList<>();
            taiKhoan.getQuyen().forEach(item -> {
                System.out.println(item.getTen());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getTen().toUpperCase()));
            });

            UserDetails userDetails = new User(taiKhoan.getUsername(), "{noop}" + taiKhoan.getPassword(), authorities);
            userDetailsManager.createUser(userDetails);
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Boolean grantRoles_UserDetails(List<Quyen> quyens, String username) {
        try{
            Boolean user_exists = userDetailsManager.userExists(username);
            if(user_exists){

                List<GrantedAuthority> authorities = new ArrayList<>();

                for (Quyen item : quyens) {
                    authorities.add(
                            new SimpleGrantedAuthority("ROLE_" + item.getTen().toUpperCase())
                    );
                }
                String password = taiKhoanRepository.findAll().stream().filter(
                        item -> item.getUsername().toUpperCase().equals(username.toUpperCase())
                ).findFirst().get().getPassword();
                UserDetails userDetails = new User(username, "{noop}" + password, authorities);
                userDetailsManager.deleteUser(username);
                userDetailsManager.createUser(userDetails);
                return true;
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
        return false;
    }

    @Override
    public void set_session_token() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            HttpSession session = httpServletRequest.getSession();
            if (!(authentication instanceof AnonymousAuthenticationToken) && session.getAttribute("username") == null) {
                List<TaiKhoan> taiKhoans = taiKhoanRepository.findAll();
                Optional<TaiKhoan> optionalTaiKhoan = taiKhoans.stream()
                        .filter(item -> item.getUsername().equals(authentication.getName())).findFirst();
                TaiKhoan taiKhoan = optionalTaiKhoan.get();
                if (taiKhoan != null) {
                    session.setAttribute("id", taiKhoan.getNhanvien_id());
                    session.setAttribute("username", taiKhoan.getUsername());
                    session.setAttribute("fullname", taiKhoan.getNhanvien().getTen());
                    session.setAttribute("roles", taiKhoan.getQuyen());
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public void del_session_token() {
        HttpSession session = httpServletRequest.getSession();
        Object session_exists = session.getAttribute("id");
        if(session_exists != null) session.removeAttribute("id");
        session_exists = session.getAttribute("username");
        if(session_exists != null) session.removeAttribute("username");
        session_exists = session.getAttribute("fullname");
        if(session_exists != null) session.removeAttribute("fullname");
        session_exists = session.getAttribute("roles");
        if(session_exists != null) session.removeAttribute("roles");
    }

    @Override
    public String get_session_fullname_token() {
        try {
            HttpSession session = httpServletRequest.getSession();
            return session.getAttribute("fullname").toString();
        } catch (Exception e) {
            System.err.println(e);
        }
        return "User Software";
    }

    @Override
    public String get_session_username_token() {
        try {
            HttpSession session = httpServletRequest.getSession();
            return session.getAttribute("username").toString();
        } catch (Exception e) {
            System.err.println(e);
        }
        return "None Username";
    }

    @Override
    public int get_session_id_token() {
        try {
            HttpSession session = httpServletRequest.getSession();
            return Integer.parseInt(session.getAttribute("id").toString());
        } catch (Exception e) {
            System.err.println(e);
        }
        return -1;
    }

    @Override
    public Object get_session_roles_token() {
        try {
            HttpSession session = httpServletRequest.getSession();
            List<Quyen> quyens = (List<Quyen>) session.getAttribute("roles");
            List<String> roles = quyens.stream().map(item -> item.getTen()).toList();
            return roles;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    @Override
    public void set_session_roles_token(List<String> roles){
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("roles", roles);
    }

    @Override
    public Object get_roles_UserDetailsManager() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
                // for(GrantedAuthority g : grantedAuthorities){
                // System.out.println(g.getAuthority());
                // }
                List<String> list = grantedAuthorities.stream().map(item -> item.getAuthority()).toList();
                return list;
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        HttpSession session = httpServletRequest.getSession();
        return session.getAttribute(key);
    }

    @Override
    public Boolean is_login() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return true;
        return false;
    }

    @Override
    public Boolean manager() {
        List<String> roles = (List<String>) get_session_roles_token();
        Optional<String> role = roles.stream().filter(item -> item.toUpperCase().equals("MANAGER")).findFirst();
        return role.isPresent();
    }

    @Override
    public Boolean user(Boolean grantedAuthority) {
        List<String> roles = (List<String>) get_roles_UserDetailsManager();
        Optional<String> role = roles.stream().filter(item -> item.toUpperCase().contains("USER")).findFirst();
        return role.isPresent();
    }


    @Override
    public Boolean admin(Boolean grantedAuthority) {
        List<String> roles = (List<String>) get_roles_UserDetailsManager();
        Optional<String> role = roles.stream().filter(item -> item.toUpperCase().contains("ADMIN")).findFirst();
        return role.isPresent();
    }

    @Override
    public Boolean manager(Boolean grantedAuthority) {
        List<String> roles = (List<String>) get_roles_UserDetailsManager();
        Optional<String> role = roles.stream().filter(item -> item.toUpperCase().contains("MANAGER")).findFirst();
        return role.isPresent();
    }

    @Override
    public String request_url() {
        return httpServletRequest.getRequestURI();
    }
}
