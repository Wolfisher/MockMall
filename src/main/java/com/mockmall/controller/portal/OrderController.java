package com.mockmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mockmall.common.Const;
import com.mockmall.common.ResponseCode;
import com.mockmall.common.ServerResponse;
import com.mockmall.pojo.User;
import com.mockmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: ShawnMall
 * @description: Order controller for alipay
 * @author: Shawn Li
 * @create: 2018-10-29 00:27
 **/

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, long orderNo, HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");

        return iOrderService.pay(user.getId(), path, orderNo);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();

        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        logger.info("Alipay callback, sign:{}, trade_status:{}, parameter:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //IMPORTANT!!!
        //check if the callback is correct and was sent from Alipay.
        //Avoid repeat notification
        params.remove("sign_type");

        try {
            boolean alipayRSAcheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSAcheckedV2) {
                return ServerResponse.createWithErrorMsg("wrong callback, illegal operation");
            }
        } catch (AlipayApiException e) {
            logger.info("alipay call exception",e);
        }

        //todo:验证支付宝回调的正确性
        //out_trade_no; total_amount; seller_id; etc
        ServerResponse response = iOrderService.alipayCallback(params);
        if (response.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, long orderNo, HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createWithError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);

        return serverResponse.isSuccess()?ServerResponse.createWithSuccess(true):ServerResponse.createWithSuccess(false);
    }
}
