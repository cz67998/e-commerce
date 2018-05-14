package com.webank.common;

/**
 * 定义服务端错误码信息
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"), // 成功
    ERROR(1, "ERROR"), // 错误
    NEED_LOGIN(10, "NEED_LOGIN"), // 需要登陆
    ILLEGAL_ARGUMENT(20, "ILLEGAL_ARGUMENT"); // 非法参数

    private int code;
    private String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
