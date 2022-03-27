package com.peteralbus.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peteralbus.domain.User;
import com.peteralbus.mapper.UserMapper;
import com.peteralbus.service.UserService;
import com.peteralbus.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.time.LocalDateTime;

/**
 * The type Photo service.
 * @author PeterAlbus
 * Created on 2022/3/26.
 */
@Service
public class UserServiceImpl implements UserService
{
    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }

    @Override
    public String register(User user)
    {
        user.setGmtCreate(LocalDateTime.now());
        user.setUserIdentity(5);
        user.setUserSalt(RandomUtil.getSalt(8));
        user.setUserPassword(SaSecureUtil.md5BySalt(user.getUserPassword(), user.getUserSalt()));
        try
        {
            if(userMapper.insert(user)>0)
            {
                return user.getUserId().toString();
            }
        }
        catch (Exception e)
        {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return "repeatAccount";
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getUserById(Long userId)
    {
        return userMapper.selectById(userId);
    }

    @Override
    public User authByMail(String userMail, String userPassword)
    {
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("user_mail",userMail);
        User user=userMapper.selectOne(userQueryWrapper);
        if(user.getUserPassword().equals(SaSecureUtil.md5BySalt(userPassword, user.getUserSalt())))
        {
            return user;
        }
        return null;
    }

    @Override
    public User authByPhone(String userPhone, String userPassword)
    {
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("user_phone",userPhone);
        User user=userMapper.selectOne(userQueryWrapper);
        if(user.getUserPassword().equals(SaSecureUtil.md5BySalt(userPassword, user.getUserSalt())))
        {
            return user;
        }
        return null;
    }

    @Override
    public int updateUser(User user)
    {
        return userMapper.updateById(user);
    }

    @Override
    public int changePassword(Long userId, String oldPassword, String newPassword)
    {
        User user=userMapper.selectById(userId);
        if(user.getUserPassword().equals(SaSecureUtil.md5BySalt(oldPassword, user.getUserSalt())))
        {
            user.setUserPassword(SaSecureUtil.md5BySalt(newPassword, user.getUserSalt()));
            return userMapper.updateById(user);
        }
        return -1;
    }
}
