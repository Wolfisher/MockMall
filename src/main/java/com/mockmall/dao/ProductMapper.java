package com.mockmall.dao;

import com.mockmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectByProductNameAndProductId(@Param("productName") String productName,
                                                  @Param("productId") Integer productId);

    List<Product> selectByProductNameAndCategoryId(@Param("productName") String productName,
                                                   @Param("categoryIdList") List<Integer> categoryIdList);
}
