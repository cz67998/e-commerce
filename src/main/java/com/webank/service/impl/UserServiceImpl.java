package com.webank.service.impl;

import com.webank.common.Const;
import com.webank.common.ServerResponse;
import com.webank.common.TokenCache;
import com.webank.dao.UserMapper;
import com.webank.entity.User;
import com.webank.service.IUserService;
import com.webank.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        // 校验用户名
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }

        // 校验密码
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误！");
        }

        // 将密码设置为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功！", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        Date date = new Date();

        // 检查用户名
        if (StringUtils.isNotBlank(username)) {
            int resultCount = userMapper.checkUsername(username);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMsg("该用户名已存在！");
            }
        } else {
            return ServerResponse.createByErrorMsg("用户名未填写！");
        }

        // 检查邮箱
        if (StringUtils.isNotBlank(email)) {
            int resultCount = userMapper.checkEmail(email);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMsg("该邮箱已注册！");
            }
        } else {
            return ServerResponse.createByErrorMsg("邮箱未填写！");
        }

        // 注册用户信息
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setCreateTime(date);
        user.setUpdateTime(date);
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败！");
        }
        return ServerResponse.createBySuccessMsg("注册成功！");
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        // 判断邮箱是否已被注册
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getUserId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMsg("该邮箱已注册，请更换新的邮箱！");
        }

        // 更新用户信息
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setUpdateTime(new Date());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount == 0) {
            return ServerResponse.createByErrorMsg("更新用户信息失败！");
        }
        return ServerResponse.createBySuccess("更新用户信息成功！", user);
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        // 校验用户名
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }

        // 根据用户名获取密保问题
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMsg("该用户未设置密保问题！");
        }
        return ServerResponse.createBySuccess("获取密保问题成功！", question);
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        // 校验密保问题
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("问题答案错误！");
        }

        // 生成token，并存入缓存
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
        return ServerResponse.createBySuccess("密保问题校验成功！", forgetToken);
    }

    @Override
    public ServerResponse<String> resetPasswordByToken(String username, String newPassword, String token) {
        // 校验token
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("token不能为空！");
        }

        // 校验用户名
        if (StringUtils.isNotBlank(username)) {
            int resultCount = userMapper.checkUsername(username);
            if (resultCount == 0) {
                return ServerResponse.createByErrorMsg("用户名不存在！");
            }
        } else {
            return ServerResponse.createByErrorMsg("用户名未填写！");
        }

        // 获取缓存的token，校验成功后重置密码
        String forgetToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("token已失效！");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMsg("重置密码成功！");
            }
        } else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取token！");
        }
        return ServerResponse.createByErrorMsg("重置密码失败！");
    }

    @Override
    public ServerResponse<String> resetPasswordByStatus(String oldPassword, String newPassword, User user) {
        // 校验旧密码
        int resultCount = userMapper.checkOldPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getUsername());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("旧密码错误！");
        }

        // 设置新密码
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("重置密码成功！");
        }
        return ServerResponse.createByErrorMsg("重置密码失败！");
    }
}
