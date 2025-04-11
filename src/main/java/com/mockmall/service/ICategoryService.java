package com.mockmall.service;

import com.mockmall.common.ServerResponse;
import com.mockmall.pojo.Category;

import java.util.List;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-09-04 17:00
 **/
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentID);

    ServerResponse updateCategoryName(Integer categoryID, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryID);

    ServerResponse<List<Integer>> selectCategoryAndChildrenByID(Integer categoryId);


}
