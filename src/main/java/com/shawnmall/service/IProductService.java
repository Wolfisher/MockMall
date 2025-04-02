package com.shawnmall.service;

import com.github.pagehelper.PageInfo;
import com.shawnmall.common.ServerResponse;
import com.shawnmall.pojo.Product;
import com.shawnmall.vo.ProductDetailVo;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-09-11 14:05
 **/

//@Service("iProductService")
public interface IProductService {

    ServerResponse saveUpdateProduct(Product product);

    ServerResponse setSalesStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetails(Integer productId);

    ServerResponse getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> productSearch(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
