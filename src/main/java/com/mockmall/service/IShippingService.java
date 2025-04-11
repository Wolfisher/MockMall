package com.mockmall.service;

import com.github.pagehelper.PageInfo;
import com.mockmall.common.ServerResponse;
import com.mockmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

/**
 * @program: ShawnMall
 * @description:
 * @author: Shawn Li
 * @create: 2018-09-26 16:23
 **/

public interface IShippingService {

    ServerResponse add(@Param("userId") Integer userId, @Param("shipping") Shipping shipping);

    ServerResponse<String> delete(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    ServerResponse update(@Param("userId") Integer userId, @Param("shipping") Shipping shipping);

    ServerResponse<Shipping> search(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    ServerResponse<PageInfo> list(@Param("userId") Integer userId, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
