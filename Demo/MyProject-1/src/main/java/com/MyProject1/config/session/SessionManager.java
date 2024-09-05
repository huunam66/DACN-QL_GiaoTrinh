package com.MyProject1.config.session;

import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;


// Quản lý các phiên
public interface SessionManager {
    String get_session_fullname_token();

    void set_session_token();

    void del_session_token();

    String get_session_username_token();

    int get_session_id_token();

    Object get_session_roles_token();
    void set_session_roles_token(List<String> roles);

    Object get_roles_UserDetailsManager();

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    void removeAttribute(String key);

    Boolean is_login();

    Boolean manager();

    Boolean admin(Boolean grantedAuthority);
    Boolean user(Boolean grantedAuthority);
    Boolean manager(Boolean grantedAuthority);

    Boolean addUserDetails(TaiKhoan taiKhoan);
    Boolean updateUserDetails(TaiKhoan taiKhoan, String old_Username);

    Boolean grantRoles_UserDetails(List<Quyen> quyens, String username);

    String request_url();

}
