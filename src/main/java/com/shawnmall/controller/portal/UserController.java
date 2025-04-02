package com.shawnmall.controller.portal;

import com.shawnmall.common.Const;
import com.shawnmall.common.ResponseCode;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.User;
import com.shawnmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/*
 *Created by Xiangyong Li
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    //User Login
    @Autowired
    private IUserService iUserService;

    @RequestMapping( value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    //User Logout
    @RequestMapping( value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createWithSuccess();
    }

    //User Registration
    @RequestMapping( value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    //Check the validation of email and password
    @RequestMapping( value = "check_validation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValidation(String str, String type) {
        return iUserService.checkValidation(str,type);
    }

    //Get the user information
    @RequestMapping( value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServerResponse.createWithSuccess(user);
        }
        return ServerResponse.createWithErrorMsg("Can't get user information");
    }

    //User reset password: get the password question
    @RequestMapping( value = "forget_psw_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPswGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    //Check the answer of the password questions
    @RequestMapping( value = "forget_psw_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPswCheckAnswer(String username, String question, String answer) {
        return iUserService.checkPswAnswer(username, question, answer);
    }

    //User reset password
    @RequestMapping( value = "forget_reset_psw.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPsw(String username, String newPsw, String forgetToken) {
        return  iUserService.forgetResetPsw(username, newPsw, forgetToken);
    }

    //User reset password after login
    @RequestMapping( value = "reset_psw.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPsw(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createWithErrorMsg("User is not exit or not logged in");
        }
        return iUserService.resetPsw(user, passwordOld, passwordNew);
    }

    //User information update
    @RequestMapping( value = "info_update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> infoUpdate(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createWithErrorMsg("User is not exit or user is logged in");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServerResponse<User> response = iUserService.infoUpdate(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    //Get detailed user information
    @RequestMapping( value = "get_detailed_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getDetailedUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(), "User log in needed, status = 10");
        }
        return iUserService.getUserInfo(currentUser.getId());
    }

}


