package com.peteralbus.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.aliyuncs.exceptions.ClientException;
import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import com.peteralbus.util.RandomUtil;
import com.peteralbus.util.RedisUtils;
import com.peteralbus.util.SmsUtil;
import com.peteralbus.util.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
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
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(userId))
        {
            return "noPermission";
        }
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

    @RequestMapping("/setPhone")
    public String setPhone(Long userId,String userPhone,String verifyCode)
    {
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(userId))
        {
            return "noPermission";
        }
        User user=userService.getUserById(userId);
        if(user==null)
        {
            return "wrongUserId";
        }
        String verifyCodeKey="verifyCode:"+userPhone;
        if(redisUtils.exists(verifyCodeKey))
        {
            if(redisUtils.get(verifyCodeKey).equals(verifyCode))
            {
                user.setUserPhone(userPhone);
                if(userService.updateUser(user)>0)
                {
                    redisUtils.remove(verifyCodeKey);
                    return "success";
                }
                return "fail";
            }
            return "wrongVerifyCode";
        }
        return "needRequestVerifyCode";
    }

    @RequestMapping("/setMail")
    public String setMail(Long userId,String userMail,String verifyCode)
    {
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(userId))
        {
            return "noPermission";
        }
        User user=userService.getUserById(userId);
        if(user==null)
        {
            return "wrongUserId";
        }
        String verifyCodeKey="verifyCode:"+userMail;
        if(redisUtils.exists(verifyCodeKey))
        {
            if(redisUtils.get(verifyCodeKey).equals(verifyCode))
            {
                user.setUserMail(userMail);
                if(userService.updateUser(user)>0)
                {
                    redisUtils.remove(verifyCodeKey);
                    return "success";
                }
                return "fail";
            }
            return "wrongVerifyCode";
        }
        return "needRequestVerifyCode";
    }

    @PostMapping("/uploadAvatar")
    public String upload(@RequestParam("file") MultipartFile file,Long userId)
    {
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(userId))
        {
            return "noPermission";
        }
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/avatar/";
        String fileName = file.getOriginalFilename();
        String type=TypeUtil.getType(fileName);
        User user=userService.getUserById(userId);
        if(user==null)
        {
            return "wrongUserId";
        }
        String newName;
        if(user.getUserPhone()!=null)
        {
            newName="avatar_"+user.getUserPhone()+"_"+UUID.randomUUID().toString().replace("-", "").toLowerCase();
        }
        else
        {
            newName="avatar_"+user.getUserMail()+"_"+UUID.randomUUID().toString().replace("-", "").toLowerCase();
        }
        if(TypeUtil.isImg(type))
        {
            newName = newName +type;
            File dest = new File(uploadPath + newName);
            try {
                file.transferTo(dest);
                user.setUserAvatar("https://file.peteralbus.com/assets/blog/imgs/avatar/"+newName);
                userService.updateUser(user);
                return "https://file.peteralbus.com/assets/blog/imgs/avatar/"+newName;
            } catch (IOException e) {
                return "上传错误:"+e.getMessage();
            }
        }
        return "typeError";
    }

    @RequestMapping("/changeUsername")
    public String changeUsername(Long userId,String username)
    {
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(userId))
        {
            return "noPermission";
        }
        User user=userService.getUserById(userId);
        if(user==null)
        {
            return "wrongUserId";
        }
        user.setUserUsername(username);
        int result=userService.updateUser(user);
        if(result>0)
        {
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/getUserById")
    public User getUserById(Long userId)
    {
        User user=userService.getUserById(userId);
        user.setUserPhone(null);
        user.setUserPassword(null);
        user.setUserMail(null);
        user.setUserSalt(null);
        return user;
    }
}
