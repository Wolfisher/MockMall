package com.shawnmall.service;

import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.User;

public interface IUserService {

    ServerResponse<User> login (String username, String password);
}
