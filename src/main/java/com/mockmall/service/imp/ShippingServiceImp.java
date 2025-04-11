package com.mockmall.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mockmall.common.ServerResponse;
import com.mockmall.dao.ShippingMapper;
import com.mockmall.pojo.Shipping;
import com.mockmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: ShawnMall
 * @description:
 * @author: Shawn Li
 * @create: 2018-09-26 16:23
 **/


@Service("iShippingService")
public class ShippingServiceImp implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping) {
        //In case of privilege escalation
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);

        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createWithSuccess("New address is added", result);
        }
        return ServerResponse.createWithErrorMsg("Address can not be added");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByUserIdShippingId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.createWithSuccessMsg("Address deletion is success.");
        }
        return ServerResponse.createWithErrorMsg("Address deletion is failed.");
    }

    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);

        if (rowCount > 0) {
            return ServerResponse.createWithSuccessMsg("New address is updated");
        }
        return ServerResponse.createWithErrorMsg("Address can not be updated");
    }

    public ServerResponse<Shipping> search(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdShippingId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createWithSuccess("Address deletion is success.",shipping);
        }
        return ServerResponse.createWithErrorMsg("Address deletion is failed.");
    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createWithSuccess(pageInfo);
    }
}
