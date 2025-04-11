package com.mockmall.service;

import com.mockmall.common.ServerResponse;
import com.mockmall.vo.CartVo;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-09-23 21:17
 **/
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> list (Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId);

    ServerResponse<Integer> getCartProductNumber(Integer userId);

}
