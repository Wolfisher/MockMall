package com.shawnmall.dao;

import com.shawnmall.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-08-28 02:53
 **/
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username, @Param("password")String password);

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    int checkPswAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    int updatePswByUsername(@Param("username")String username,@Param("newPsw")String newPsw);

    int checkPsw(@Param(value="password")String password,@Param("userId")Integer userId);

    int checkEmailByUserID(@Param(value="email")String email,@Param(value="userId")Integer userId);
}