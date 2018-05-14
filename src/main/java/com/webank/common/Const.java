package com.webank.common;

/**
 * 定义常量
 */
public class Const {
    public static final String CURRENT_USER = "currentUser"; // 当前用户

    // 用户类型
    public interface Role {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }
}
