package com.mockmall.service.imp;

import com.mockmall.common.Const;
import com.mockmall.common.ServerResponse;
import com.mockmall.common.TokenCache;
import com.mockmall.dao.UserMapper;
import com.mockmall.pojo.User;
import com.mockmall.service.IUserService;
import com.mockmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @program: ShawnMall
 * @description: This is the application for user service
 * @author: Shawn Li
 * @create: 2018-08-13 15:25
 **/
@Service("iUserService")
public class UserServiceImp implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    //User Login
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);

        if (resultCount == 0){
            return ServerResponse.createWithErrorMsg("Username is not exit");
        }

        //MD5 password Encryption
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null){
            return ServerResponse.createWithErrorMsg("Wrong Password");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createWithSuccess("Login Successful", user);
    }

    //User Registration
    public ServerResponse<String> register(User user){

        ServerResponse nameValidation = this.checkValidation(user.getUsername(),Const.USERNAME);
        ServerResponse emailValidation = this.checkValidation(user.getEmail(),Const.EMAIL);
        if (!nameValidation.isSuccess()){
            return nameValidation;
        }
        if (!emailValidation.isSuccess()){
            return emailValidation;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5 Encryption Using MD5Util Written By Geely
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int userCount = userMapper.insert(user);
        if (userCount == 0){
            return ServerResponse.createWithErrorMsg("Registration Failed");
        }

        return ServerResponse.createWithSuccessMsg("Registration success !");
    }

    //Check validation of the username and email
    public ServerResponse<String> checkValidation(String str, String type){
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                int nameCount = userMapper.checkUsername(str);

                if (nameCount > 0){
                    return ServerResponse.createWithErrorMsg("Username is already exit");
                }
            }
            if (Const.EMAIL.equals(type)){
                int emailCount = userMapper.checkEmail(str);

                if (emailCount > 0){
                    return ServerResponse.createWithErrorMsg("User Email is already exit");
                }
            }

        } else {
            return ServerResponse.createWithErrorMsg("Invalid Parameter");
        }
        return ServerResponse.createWithSuccessMsg("Validation verified");
    }

    //Get the password question if the user forget the password
    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValidation(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createWithErrorMsg("User doesn't exit");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createWithSuccess(question);
        }
        return ServerResponse.createWithErrorMsg("Question doesn't exit");
    }

    //Check the answer of the password question
    public ServerResponse<String> checkPswAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkPswAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetPswToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetPswToken);
            return ServerResponse.createWithSuccessMsg(forgetPswToken);
        }
        return ServerResponse.createWithErrorMsg("Wrong Answer");
    }

    //Reset the password
    public ServerResponse<String> forgetResetPsw(String username, String newPsw, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createWithErrorMsg("parameter error, token needed");
        }

        //Check the username in case of blank username to protect token
        ServerResponse validResponse = this.checkValidation(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createWithErrorMsg("User doesn't exit");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        //check token, may be invalid of expired
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createWithErrorMsg("Invalid token");
        }

        //
        if (StringUtils.equals(forgetToken, token)) {
            String md5Psw = MD5Util.MD5EncodeUtf8(newPsw);
            int rowCount = userMapper.updatePswByUsername(username, md5Psw);

            if (rowCount > 0) {
                return ServerResponse.createWithSuccessMsg("Password updated");
            }
        } else {
            ServerResponse.createWithErrorMsg("Invalid token. Please try again");
        }
        return ServerResponse.createWithErrorMsg("Password update failed");

    }

    //Reset password after login
    public ServerResponse<String> resetPsw(User user, String passwordOld, String passwordNew) {
        //Prevent horizontal privilege escalation, where a normal user accesses functions or content reserved for other normal users
        //(e.g. Internet Banking User A accesses the Internet bank account of User B)
        int resultCount = userMapper.checkPsw(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        int updateCount = userMapper.updateByPrimaryKeySelective(user);

        if (resultCount == 0) {
            return ServerResponse.createWithErrorMsg("Wrong Password");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));

        if (updateCount > 0) {
            return ServerResponse.createWithSuccessMsg("Password updated!");
        }
        return ServerResponse.createWithErrorMsg("Password update failed");
    }

    //Update user information
    public ServerResponse<User> infoUpdate(User user) {
        //username can not be updated
        //Email must be checked in case of the email address is exited and not the current email
        int resultCount = userMapper.checkEmailByUserID(user.getEmail(), user.getId());
        User updatedUser = new User();

        if (resultCount > 0) {
            return ServerResponse.createWithErrorMsg("Email is already exited, please change email and try again");
        }

        updatedUser.setId(user.getId());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setQuestion(user.getQuestion());
        updatedUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updatedUser);

        if (updateCount > 0) {
            return ServerResponse.createWithSuccess("Update sucess", updatedUser);
        }
        return ServerResponse.createWithErrorMsg("Could not update infromation");
    }

    //Get detailed user information
    public ServerResponse<User> getUserInfo(Integer userID) {
        User user = userMapper.selectByPrimaryKey(userID);
        if (user == null) {
            return ServerResponse.createWithErrorMsg("User is not exit");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createWithSuccess(user);
    }

    //Check if the user is administration --backend
    public ServerResponse<User> checkAdminRole(User user) {
        if (user.getRole().intValue() == Const.Role.ROLE_ADMIN && user !=null) {
            return ServerResponse.createWithSuccess();
        }
        return ServerResponse.createWithError();
    }
}
