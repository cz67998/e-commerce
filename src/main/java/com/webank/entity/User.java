package com.webank.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */
@Data
public class User {
    private Integer userId;// 用户ID

    private String username;// 用户名

    private String password;// 密码，MD5加密

    private String email;// 邮箱

    private String phone;// 电话

    private String question;// 找回密码问题

    private String answer;// 找回密码答案

    private Integer role;// 角色：0-管理员，1-普通用户

    private Date createTime;// 创建时间

    private Date updateTime;// 更新时间
}