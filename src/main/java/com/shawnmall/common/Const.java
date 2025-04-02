package com.shawnmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @program: ShawnMall
 * @description: A constant class
 * @author: Shawn Li
 * @create: 2018-08-14 16:37
 **/

public class Const {
    public static final String CURRENT_USER = "current_user";

    public interface Role {
        int ROLE_CUSTOMER = 0; //common customers
        int ROLE_ADMIN = 1; //Administration
    }

    public interface ProductListOrderBy {
        Set<String> price_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface Cart {
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    }
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public enum ProductStatusEnum {
        ON_SALE(1, "Online");

        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }


        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum {

        CANCELLED(0,"It's cancelled"),
        UNPAID(10,"Its unpaid"),
        PAID(20,"it's paid"),
        SHIPPED(40,"It's on the way"),
        COMPLETED(50,"completed"),
        CLOSED(60,"order is closed");

        OrderStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum AlipayCallback {
        TRADE_STATUS_WAIT_BUYER_PAY("WAIT_BUYER_PAY"),
        TRADE_CLOSED("TRADE_CLOSED"),
        TRADE_STATUS_TRADE_SUCCESS("TRADE_SUCCESS"),
        TRADE_FINISHED("TRADE_FINISHED"),
        RESPONSE_SUCCESS("success"),
        RESPONSE_FAILED("failed");



        private String tradeStatus;

        AlipayCallback(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }
    }

    public enum PayPlatform {
        ALIPAY(1,"alipay");

        PayPlatform(int code, String value){
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
