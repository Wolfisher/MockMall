package com.shawnmall.service;

import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.User;

public interface IUserService {

    ServerResponse<User> login (String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValidation(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkPswAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPsw(String username, String newPsw, String forgetToken);

    ServerResponse<String> resetPsw(User user, String passwordOld, String passwordNew);

    ServerResponse<User> infoUpdate(User user);

    ServerResponse<User> getUserInfo(Integer userId);

    ServerResponse<User> checkAdminRole(User user);
}
