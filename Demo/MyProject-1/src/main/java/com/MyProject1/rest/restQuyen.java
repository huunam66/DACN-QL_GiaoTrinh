package com.MyProject1.rest;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load/api")
public class restQuyen {

    @Autowired
    private SessionManager session;

    @Autowired
    TaiKhoanService taiKhoanService;

    @RequestMapping(value = "/roles/current/admin", method = RequestMethod.GET)
    public Map<String, Object> roles_current_admin() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", session.admin(true));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }

    @RequestMapping(value = "/roles/current/manager", method = RequestMethod.GET)
    public Map<String, Object> roles_current() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", session.manager(true));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }

    @RequestMapping(value = "/roles/is/manager", method = RequestMethod.POST)
    public Map<String, Object> roles_is_manager(@RequestBody ModelMap model) {
        Map<String, Object> map = new HashMap<>();
        try {
            int id = Integer.parseInt(model.getAttribute("id").toString());
            map.put("status", taiKhoanService.roles_is_manager(id));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }

    @RequestMapping(value = "/account/exists", method = RequestMethod.POST)
    public Map<String, Object> account_exists(@RequestBody ModelMap model) {
        Map<String, Object> map = new HashMap<>();
        try {
            int id = Integer.parseInt(model.getAttribute("id").toString());
            map.put("status", taiKhoanService.account_exists(id));
        } catch (Exception e) {
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }
        return map;
    }
}
