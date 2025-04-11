package com.mockmall.service;

import com.mockmall.common.ServerResponse;

import java.util.Map;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-10-29 00:32
 **/
public interface IOrderService {

    ServerResponse pay(Integer userId, String path, long orderNo);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
