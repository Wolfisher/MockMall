package com.shawnmall.common;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-08-13 15:44
 **/
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String description;

    //constructor
    ResponseCode(int code, String description){
        this.code = code;
        this.description = description;
    }

    //get the value of code and description

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
