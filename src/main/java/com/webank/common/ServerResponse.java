package com.webank.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 定义服务端响应格式
 *
 * @param <T>
 */
public class ServerResponse<T> {
    private int status; // 状态
    private String msg; // 信息
    private T data; // 数据

    ServerResponse(int status) {
        this.status = status;
    }

    ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    @JsonIgnore
    public boolean isError() {
        return this.status == ResponseCode.ERROR.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMsg(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMsg(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String msg) {
        return new ServerResponse<T>(errorCode, msg);
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
