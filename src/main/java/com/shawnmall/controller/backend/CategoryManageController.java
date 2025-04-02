package com.shawnmall.controller.backend;

import com.shawnmall.common.Const;
import com.shawnmall.common.ResponseCode;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.User;
import com.shawnmall.service.ICategoryService;
import com.shawnmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: ShawnMall
 * @description: Manage the catagory of product
 * @author: Shawn Li
 * @create: 2018-09-04 14:06
 **/

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    //Add a new category to the system
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(),"User is not logged in, please log in now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createWithErrorMsg("Access denied, need to be admin");
        }
    }

    //Reset category name
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(),"User is not logged in, please log in now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createWithErrorMsg("Access denied, need to be admin");
        }
    }

    //Get the child categories by category ID
    @RequestMapping("get_children_parallel_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(),"User is not logged in, please log in now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createWithErrorMsg("Access denied, need to be admin");
        }
    }

    //Get current category ID and all the children ID Recursively
    @RequestMapping("get_category_and_deep_children_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(),"User is not logged in, please log in now");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.selectCategoryAndChildrenByID(categoryId);
        } else {
            return ServerResponse.createWithErrorMsg("Access denied, need to be admin");
        }
    }
}
