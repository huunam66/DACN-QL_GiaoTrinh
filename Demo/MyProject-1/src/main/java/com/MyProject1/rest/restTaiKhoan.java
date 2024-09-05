package com.MyProject1.rest;

import com.MyProject1.service.TaiKhoanService;

import groovy.transform.Undefined.EXCEPTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restTaiKhoan {

    @Autowired
    TaiKhoanService taiKhoanService;

    @RequestMapping(value = "/account/username", method = RequestMethod.POST)
    public Map<String, Object> username(@RequestBody ModelMap model) {
        Map<String, Object> map = new HashMap<>();
        try {
            int id = Integer.parseInt(model.getAttribute("id").toString());
            map.put("username", taiKhoanService.get_username(id));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }

    @RequestMapping(value = "/account/change/password", method = RequestMethod.PUT)
    public Map<String, Object> account_change_password(@RequestBody ModelMap model) {
        Map<String, Object> map = new HashMap<>();
        try {
            int id = Integer.parseInt(model.getAttribute("id").toString());
            String pw = model.getAttribute("pw").toString();
            Boolean change = taiKhoanService.change_pw(id, pw);
            if (change) {
                map.put("status", "Đổi mật khẩu thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }

        map.put("status", "Đổi mật khẩu thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");

        return map;

    }

    @RequestMapping(value = "/account/constraint/username", method = RequestMethod.POST)
    public Map<String, Object> account_constraint_username(@RequestBody ModelMap model) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = model.get("username").toString();
            map.put("status", taiKhoanService.constraint_username(username));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }

    @RequestMapping(value = "/account/create", method = RequestMethod.POST)
    public Map<String, Object> account_create(@RequestBody ModelMap modelMap) {
        Map<String, Object> map = new HashMap<>();
        try {
            Boolean create = taiKhoanService.create_account(
                    Integer.parseInt(modelMap.getAttribute("id").toString()),
                    modelMap.getAttribute("username").toString(),
                    modelMap.getAttribute("password").toString());

            if (create) {
                map.put("status", "Tạo tài khoản thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        map.put("status", "Tạo tài khoản thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");
        return map;
    }
}
