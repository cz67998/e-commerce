package com.webank.controller;

import com.webank.common.Const;
import com.webank.common.ServerResponse;
import com.webank.entity.User;
import com.webank.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户管理模块
 */
@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 用户登陆
     *
     * @param session
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(HttpSession session, String username, String password) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户登出
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = userService.register(user);
        return response;
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息！");
        }
        return ServerResponse.createBySuccess("获取用户信息成功！", user);
    }

    /**
     * 更新用户信息
     *
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("用户未登陆，无法更新用户信息！");
        }
        user.setUserId(currentUser.getUserId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateUserInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 获取密保问题
     *
     * @param username 用户名
     * @return
     */
    @RequestMapping(value = "getSecurityQuestion", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getSecurityQuestion(String username) {
        ServerResponse<String> response = userService.selectQuestion(username);
        return response;
    }

    /**
     * 校验密保问题
     *
     * @param username 用户名
     * @param question 找回密码问题
     * @param answer   找回密码答案
     * @return
     */
    @RequestMapping(value = "verifySecurityQuestion", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> verifySecurityQuestion(String username, String question, String answer) {
        ServerResponse<String> response = userService.checkAnswer(username, question, answer);
        return response;
    }

    /**
     * 根据token重置密码
     *
     * @param username    用户名
     * @param newPassword 新密码
     * @param token
     * @return
     */
    @RequestMapping(value = "resetPasswordByToken", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswordByToken(String username, String newPassword, String token) {
        ServerResponse<String> response = userService.resetPasswordByToken(username, newPassword, token);
        return response;
    }

    /**
     * 根据登陆状态重置密码
     *
     * @param session
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @RequestMapping(value = "resetPasswordByStatus", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswordByStatus(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登陆，请重新登陆！");
        }
        ServerResponse<String> response = userService.resetPasswordByStatus(oldPassword, newPassword, user);
        return response;
    }
}
