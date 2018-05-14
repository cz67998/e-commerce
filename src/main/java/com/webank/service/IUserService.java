package com.webank.service;

import com.webank.common.ServerResponse;
import com.webank.entity.User;

public interface IUserService {
    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    ServerResponse<User> updateUserInfo(User user);

    /**
     * 获取密保问题
     *
     * @param username 用户名
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 校验密保问题
     *
     * @param username 用户名
     * @param question 找回密码问题
     * @param answer   找回密码答案
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 根据token重置密码
     *
     * @param username    用户名
     * @param newPassword 新密码
     * @param token
     * @return
     */
    ServerResponse<String> resetPasswordByToken(String username, String newPassword, String token);

    /**
     * 根据登陆状态重置密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param user
     * @return
     */
    ServerResponse<String> resetPasswordByStatus(String oldPassword, String newPassword, User user);
}
