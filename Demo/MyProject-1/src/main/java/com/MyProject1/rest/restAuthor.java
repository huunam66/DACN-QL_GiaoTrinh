package com.MyProject1.rest;

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
public class restAuthor {

    @Autowired
    TaiKhoanService taiKhoanService;
    @RequestMapping(value = "/grant/roles", method = RequestMethod.PUT)
    public Map<String, Object> grant_roles(@RequestBody ModelMap model){
        Map<String, Object> map = new HashMap<>();
        try{
            int id = Integer.parseInt(model.getAttribute("id").toString());
            String new_role = model.getAttribute("new_role").toString();
            Boolean granted = taiKhoanService.update_roles_grant(id, new_role);
            if(granted){
                map.put("status", "Phân quyền thành công !");
                map.put("color", "#46ac4a");
                map.put("gif", "/pictures/main/icons8-success.gif");
                map.put("static", "/pictures/main/icons8-success-48.png");
                return map;
            }
        }
        catch (Exception e){
            map.put("Message", e.getMessage());
            map.put("StackTrace", e.getStackTrace());
        }

        map.put("status", "Phân quyền thất bại !");
        map.put("color", "red");
        map.put("gif", "/pictures/main/icons8-error.gif");
        map.put("static", "/pictures/main/icons8-error-48.png");

        return map;
    }
}
