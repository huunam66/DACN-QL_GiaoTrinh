package com.MyProject1.config.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Error_Request implements ErrorController {

    @Autowired
    private HttpServletRequest servletRequest;

    @GetMapping("/error")
    public String error(Model model) {
//        System.err.println("afasfa");
        Object status = servletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
//            System.out.println(statusCode);
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "ui/error/page-not-found";
            }

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "ui/error/internal-server-error";
            }

            if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                return "ui/error/not-allowed";
            }

            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "ui/error/bad-request";
            }
        }
        return "ui/error/server-unknow";
    }
}
