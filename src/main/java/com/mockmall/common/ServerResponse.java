package com.mockmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @program: ShawnMall
 * @description: ServerResponse
 * @author: Shawn Li
 * @create: 2018-08-13 15:28
 **/

//make sure the key of null serializable object is not included in Json file.
@JsonSerialize( include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private T data;
    private String msg;

    //constructor
    private ServerResponse (int status) {
        this.status = status;
    }
    private ServerResponse (int status, T data) {
        this.status = status;
        this.data = data;
    }
    private ServerResponse (int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse (int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    //ignore this in the Json serializable result
    //check if the Response is success
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    //public access to the parameters
    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    //Encapsulation
    //Create successfully
    public static <T> ServerResponse<T> createWithSuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createWithSuccessMsg(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createWithSuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createWithSuccess(String msg,T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    //create with error
    public static <T> ServerResponse<T> createWithError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription());
    }

    public static <T> ServerResponse<T> createWithErrorMsg(String errorMsg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMsg);
    }

    public static <T> ServerResponse<T> createWithError(int errorCode, String errorMsg) {
        return new ServerResponse<T>(errorCode, errorMsg);
    }
}
