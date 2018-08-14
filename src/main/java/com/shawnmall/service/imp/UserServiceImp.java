package com.shawnmall.service.imp;

import com.shawnmall.common.ServerResponse;
import com.shawnmall.dao.UserMapper;
import com.shawnmall.pojo.User;
import com.shawnmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: ShawnMall
 * @description: This is the application for user service
 * @author: Shawn Li
 * @create: 2018-08-13 15:25
 **/

public class UserServiceImp implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);

        if (resultCount == 0){
            return ServerResponse.createWithErrormsg("Username is not exit");
        }
        //TODO MD5 password


        return null;
    }
}
