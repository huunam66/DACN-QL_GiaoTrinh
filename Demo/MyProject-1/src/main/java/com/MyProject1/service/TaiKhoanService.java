package com.MyProject1.service;

import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;

import java.util.List;

public interface TaiKhoanService {
    TaiKhoan findById(int id);
    TaiKhoan save(TaiKhoan taiKhoan);
    List<TaiKhoan> List_TaiKhoan();

    Boolean account_exists(int id);

    String get_username(int id);

    Boolean change_pw(int id, String pw);

    Boolean constraint_username(String username);
    Boolean update_account(int id, String username);
    Boolean create_account(int id, String username, String password);

    Boolean update_roles_grant(int id, String new_role);

    Boolean roles_is_manager(int id);
}
