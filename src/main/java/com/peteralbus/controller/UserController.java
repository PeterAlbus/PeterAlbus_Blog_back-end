package com.peteralbus.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type User controller.
 * @author PeterAlbus
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController
{
    UserService userService;

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    @RequestMapping("/mailLogin")
    public SaTokenInfo doLogin(String userMail, String userPassword)
    {
        User user=userService.authByMail(userMail, userPassword);
        if(user!=null)
        {
            StpUtil.login(user.getUserId());
            return StpUtil.getTokenInfo();
        }
        return null;
    }

    @RequestMapping("/isLogin")
    public User isLogin()
    {
        if(StpUtil.isLogin())
        {
            Long userId=Long.valueOf((String) StpUtil.getLoginId());
            return userService.getUserById(userId);
        }
        return null;
    }

    @RequestMapping("/logout")
    public String logout()
    {
        StpUtil.logout();
        return "logout";
    }

    @RequestMapping("/register")
    public String register(User user)
    {
        return userService.register(user);
    }

    @RequestMapping("/changePassword")
    public String changePassword(Long userId,String oldPassword,String newPassword)
    {
        int result=userService.changePassword(userId,oldPassword,newPassword);
        if(result==-1)
        {
            return "wrongPassword";
        }
        if(result==0)
        {
            return "error";
        }
        StpUtil.logout(userId);
        return "success";
    }
}
