package com.shawnmall.service.imp;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.dao.CategoryMapper;
import com.shawnmall.pojo.Category;
import com.shawnmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Set;

/**
 * @program: ShawnMall
 * @description: Implement category service
 * @author: Shawn Li
 * @create: 2018-09-04 14:27
 **/
@Service("iCategoryService")

public class CategoryServiceImp implements ICategoryService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);
    @Autowired
    private CategoryMapper categoryMapper;

    //Add new category
    public ServerResponse addCategory(String categoryName, Integer parentID) {
        if (parentID == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createWithErrorMsg("Add category failure, parameter is not correct");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentID);
        category.setStatus(true);//indicate that this category can be used

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createWithSuccessMsg("Category added successfully");
        }
        return ServerResponse.createWithErrorMsg("Category is failed to add");
    }

    //Update the category name
    public ServerResponse updateCategoryName(Integer categoryID, String categoryName) {
        if (categoryID == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createWithErrorMsg("Update category failure, parameter is not correct");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryID);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createWithSuccessMsg("Update category success");
        }
        return ServerResponse.createWithErrorMsg("Update failure");
    }

    //Get the child category on the same level
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryID) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryID);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("Current children category is not found");
        }
        return ServerResponse.createWithSuccess(categoryList);
    }

    //Find the category node ID and its child ID
    public ServerResponse<List<Integer>> selectCategoryAndChildrenByID(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createWithSuccess(categoryIdList);
    }

    //Using recursion to get all the child category
    public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryID) {
        Category category = categoryMapper.selectByPrimaryKey(categoryID);
        if (category != null) {
             categorySet.add(category);
        }
        //exit point for recursion algorithm(always remember to make sure of this)
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryID);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }

}
