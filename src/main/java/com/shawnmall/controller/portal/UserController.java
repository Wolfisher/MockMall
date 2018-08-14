package com.shawnmall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/*
 *Created by Xiangyong Li
 */
@Controller
@RequestMapping("/usr/")
public class UserController {

    /*
    User Login
     */

    @RequestMapping( value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, HttpSession session){
    //service call --> Mybatis -> dao

        return null;
    }
}
