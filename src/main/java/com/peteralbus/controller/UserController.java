package com.peteralbus.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.aliyuncs.exceptions.ClientException;
import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import com.peteralbus.util.RandomUtil;
import com.peteralbus.util.RedisUtils;
import com.peteralbus.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The type User controller.
 * @author PeterAlbus
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController
{
    private UserService userService;
    private RedisUtils redisUtils;
    private JavaMailSender javaMailSender;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils)
    {
        this.redisUtils = redisUtils;
    }

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/mailLogin")
    public SaTokenInfo mailLogin(String userMail, String userPassword)
    {
        User user=userService.authByMail(userMail, userPassword);
        if(user!=null)
        {
            StpUtil.login(user.getUserId());
            return StpUtil.getTokenInfo();
        }
        return null;
    }

    @RequestMapping("/phoneLogin")
    public SaTokenInfo phoneLogin(String userPhone, String userPassword)
    {
        User user=userService.authByPhone(userPhone, userPassword);
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
            User user=userService.getUserById(userId);
            user.setUserPassword(null);
            user.setUserSalt(null);
            return user;
        }
        return null;
    }

    @RequestMapping("/logout")
    public String logout()
    {
        StpUtil.logout();
        return "logout";
    }

    @RequestMapping("/applyMailVerifyCode")
    public String applyMailVerifyCode(String account)
    {
        if(redisUtils.exists("verifyCode:"+account))
        {
            return "haveVerifyCode";
        }
        if(userService.haveAccount(account))
        {
            return "haveAccount";
        }
        String verifyCode=RandomUtil.generateVerifyCode(6);
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom("wuhongdb@163.com","PeterAlbus");
            helper.setTo(account);
            helper.setSubject("[验证码]PeterAlbus的博客注册");
            helper.setText("您正在注册PeterAlbus的个人博客账号，验证码为："+verifyCode+"，有效期10分钟。若非本人操作，请忽略此邮件！",false);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "sendMailFail";
        }
        redisUtils.set("verifyCode:"+account,verifyCode,10L, TimeUnit.MINUTES);
        return "sendMailSuccess";
    }

    @RequestMapping("/applyPhoneVerifyCode")
    public String applyPhoneVerifyCode(String account)
    {
        if(redisUtils.exists("verifyCode:"+account))
        {
            return "haveVerifyCode";
        }
        if(userService.haveAccount(account))
        {
            return "haveAccount";
        }
        final String success ="smsSendSuccess";
        String result="";
        String verifyCode=RandomUtil.generateVerifyCode(4);
        try
        {
            result=SmsUtil.sendSms(account,verifyCode);
            if(Objects.equals(result, success))
            {
                redisUtils.set("verifyCode:"+account,verifyCode,10L, TimeUnit.MINUTES);
                return "sendSmsSuccess";
            }
        }
        catch (ClientException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/register")
    public String register(User user,String verifyCode)
    {
        String verifyCodeKey;
        if(user.getUserMail()!=null)
        {
            verifyCodeKey="verifyCode:"+user.getUserMail();
        }
        else if(user.getUserPhone()!=null)
        {
            verifyCodeKey="verifyCode:"+user.getUserPhone();
        }
        else
        {
            return "needRequestVerifyCode";
        }
        if(redisUtils.exists(verifyCodeKey))
        {
            if(redisUtils.get(verifyCodeKey).equals(verifyCode))
            {
                redisUtils.remove(verifyCodeKey);
                return userService.register(user);
            }
            return "wrongVerifyCode";
        }
        return "needRequestVerifyCode";
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
