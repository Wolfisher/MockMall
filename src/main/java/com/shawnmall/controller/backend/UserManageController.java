package com.shawnmall.controller.backend;

import com.shawnmall.common.Const;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.User;
import com.shawnmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: ShawnMall
 * @description: User Management
 * @author: Shawn Li
 * @create: 2018-08-28 00:16
 **/
@Controller
@RequestMapping("/manage/user")

public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            }
        }
        return ServerResponse.createWithErrorMsg("user is not a admin, can't login");
    }

}
