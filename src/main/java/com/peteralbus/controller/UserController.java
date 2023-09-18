package com.peteralbus.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.aliyuncs.exceptions.ClientException;
import com.peteralbus.domain.Result;
import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import com.peteralbus.util.*;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final JavaMailSender javaMailSender;

    @RequestMapping("/mailLogin")
    public Result<?> mailLogin(String userMail, String userPassword) {
        if (!userService.haveAccount(userMail)) {
            return ResultUtil.error(400, "该邮箱尚未注册！");
        }
        User user = userService.authByMail(userMail, userPassword);
        if (user != null) {
            StpUtil.login(user.getUserId());
            return ResultUtil.success(StpUtil.getTokenInfo());
        }
        return ResultUtil.error(400, "密码错误或登陆方式错误（手机/邮箱）");
    }

    @RequestMapping("/phoneLogin")
    public Result<?> phoneLogin(String userPhone,String userPassword) {
        if (!userService.haveAccount(userPhone)) {
            return ResultUtil.error(400, "该手机号尚未注册！");
        }
        User user = userService.authByPhone(userPhone, userPassword);
        if (user != null) {
            StpUtil.login(user.getUserId());
            return ResultUtil.success(StpUtil.getTokenInfo());
        }
        return ResultUtil.error(400, "密码错误或登陆方式错误（手机/邮箱）");
    }

    @RequestMapping("/isLogin")
    public Result<?> isLogin() {
        if (StpUtil.isLogin()) {
            Long userId = Long.valueOf((String) StpUtil.getLoginId());
            User user = userService.getUserById(userId);
            user.setUserPassword(null);
            user.setUserSalt(null);
            return ResultUtil.success(user);
        }
        return ResultUtil.error(400, "未登录");
    }

    @RequestMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return ResultUtil.success(null);
    }

    @RequestMapping("/applyMailVerifyCode")
    public Result<?> applyMailVerifyCode(String account) {
        if (redisUtil.exists("verifyCode:" + account)) {
            return ResultUtil.error(400, "验证码已发送，请10分钟后重试");
        }
        if (userService.haveAccount(account)) {
            return ResultUtil.error(400, "该邮箱已注册");
        }
        String verifyCode = RandomUtil.generateVerifyCode(6);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("wuhongdb@163.com", "PeterAlbus");
            helper.setTo(account);
            helper.setSubject("[验证码]PeterAlbus的博客注册");
            helper.setText("您正在注册PeterAlbus的个人博客账号，验证码为：" + verifyCode + "，有效期10分钟。若非本人操作，请忽略此邮件！", false);
            javaMailSender.send(message);
        }
        catch (Exception e) {
            System.out.println("发送邮件失败：" + e.getMessage());
            return ResultUtil.error(500, "邮件发送失败");
        }
        redisUtil.set("verifyCode:" + account, verifyCode, 10L, TimeUnit.MINUTES);
        return ResultUtil.success(null);
    }

    @RequestMapping("/applyPhoneVerifyCode")
    public Result<?> applyPhoneVerifyCode(String account) {
        if (redisUtil.exists("verifyCode:" + account)) {
            return ResultUtil.error(400, "验证码已发送，请10分钟后重试");
        }
        if (userService.haveAccount(account)) {
            return ResultUtil.error(400, "该手机号已注册");
        }
        String result = "";
        String verifyCode = RandomUtil.generateVerifyCode(4);
        try {
            result = SmsUtil.sendSms(account, verifyCode);
            if (result.equals("smsSendSuccess")) {
                redisUtil.set("verifyCode:" + account, verifyCode, 10L, TimeUnit.MINUTES);
                return ResultUtil.success(null);
            }
        }
        catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        return ResultUtil.error(500, "短信发送失败，请检查手机号或联系管理员");
    }

    @RequestMapping("/applyResetPasswordVerifyCode")
    public Result<?> applyResetPasswordVerifyCode(String account,String type) {
        if (redisUtil.exists("verifyCode_reset:" + account)) {
            return ResultUtil.error(400, "验证码已发送，请10分钟后重试");
        }
        if (!userService.haveAccount(account)) {
            return ResultUtil.error(400, "该账号尚未注册");
        }
        String verifyCode = RandomUtil.generateVerifyCode(6);
        try {
            if (Objects.equals(type, "mail")) {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("wuhongdb@163.com", "PeterAlbus");
                helper.setTo(account);
                helper.setSubject("[验证码]PeterAlbus的博客");
                helper.setText("您正在重置PeterAlbus的个人博客账号密码，验证码为：" + verifyCode + "，有效期10分钟。若非本人操作，请忽略此邮件！", false);
                javaMailSender.send(message);
            }
            else {
                String result = SmsUtil.sendSms(account, verifyCode);
                if (!result.equals("smsSendSuccess")) {
                    return ResultUtil.error(500, "短信发送失败，请检查手机号或联系管理员");
                }
            }
        }
        catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("发送邮件失败：" + e.getMessage());
            return ResultUtil.error(500, "邮件发送失败");
        }
        redisUtil.set("verifyCode_reset:" + account, verifyCode, 10L, TimeUnit.MINUTES);
        return ResultUtil.success(null);
    }

    @RequestMapping("/register")
    public Result<?> register(User user, String verifyCode) {
        String verifyCodeKey;
        if (user.getUserMail() != null) {
            verifyCodeKey = "verifyCode:" + user.getUserMail();
        } else if (user.getUserPhone() != null) {
            verifyCodeKey = "verifyCode:" + user.getUserPhone();
        } else {
            return ResultUtil.error(400, "注册失败，信息中不包含手机/邮箱");
        }
        if (redisUtil.exists(verifyCodeKey)) {
            if (redisUtil.get(verifyCodeKey).equals(verifyCode)) {
                redisUtil.remove(verifyCodeKey);
                return ResultUtil.success(userService.register(user));
            }
            return ResultUtil.error(400, "验证码错误");
        }
        return ResultUtil.error(400, "请先请求验证码！");
    }

    @RequestMapping("/changePassword")
    public Result<?> changePassword(Long userId, String oldPassword, String newPassword) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(userId)) {
            return ResultUtil.error(403, "无权修改密码");
        }
        int result = userService.changePassword(userId, oldPassword, newPassword);
        if (result == -1) {
            return ResultUtil.error(400, "原密码错误");
        } else if (result == 0) {
            return ResultUtil.error(500, "密码修改失败，请联系管理员");
        }
        StpUtil.logout(userId);
        return ResultUtil.success(null, "密码修改成功，请重新登陆");
    }

    @RequestMapping("/setPhone")
    public Result<?> setPhone(Long userID, String userPhone, String verifyCode) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(userID)) {
            return ResultUtil.error(403, "无权修改手机号，请检查登陆状态");
        }
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResultUtil.error(400, "用户ID不存在");
        }
        String verifyCodeKey = "verifyCode:" + userPhone;
        if (redisUtil.exists(verifyCodeKey)) {
            if (redisUtil.get(verifyCodeKey).equals(verifyCode)) {
                user.setUserPhone(userPhone);
                if (userService.updateUser(user) > 0) {
                    redisUtil.remove(verifyCodeKey);
                    return ResultUtil.success(null);
                }
                return ResultUtil.error(500, "手机号修改失败，请联系管理员");
            }
            return ResultUtil.error(400, "验证码错误");
        }
        return ResultUtil.error(400, "请先请求验证码！");
    }

    @RequestMapping("/setMail")
    public Result<?> setMail(Long userID, String userMail, String verifyCode) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(userID)) {
            return ResultUtil.error(403, "无权修改邮箱，请检查登陆状态");
        }
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResultUtil.error(400, "用户ID不存在");
        }
        String verifyCodeKey = "verifyCode:" + userMail;
        if (redisUtil.exists(verifyCodeKey)) {
            if (redisUtil.get(verifyCodeKey).equals(verifyCode)) {
                user.setUserMail(userMail);
                if (userService.updateUser(user) > 0) {
                    redisUtil.remove(verifyCodeKey);
                    return ResultUtil.success(null);
                }
                return ResultUtil.error(500, "邮箱修改失败，请联系管理员");
            }
            return ResultUtil.error(400, "验证码错误");
        }
        return ResultUtil.error(400, "请先请求验证码！");
    }

    @RequestMapping("/resetPassword")
    public Result<?> resetPassword(String account, String verifyCode, String newPassword) {
        User user = userService.getUserByAccount(account);
        String verifyCodeKey = "verifyCode_reset:" + account;
        if (redisUtil.exists(verifyCodeKey)) {
            if (redisUtil.get(verifyCodeKey).equals(verifyCode)) {
                int result = userService.resetPassword(user.getUserId(), newPassword);
                if(result == 1) {
                    redisUtil.remove(verifyCodeKey);
                    return ResultUtil.success(null);
                }
                return ResultUtil.error(500, "密码重置出现异常，请联系管理员");
            }
            return ResultUtil.error(400, "验证码错误");
        }
        return ResultUtil.error(400, "请先请求验证码！");
    }

    @RequestMapping("/uploadAvatar")
    public Result<?> upload(@RequestParam("file") MultipartFile file, Long userId) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(userId)) {
            return ResultUtil.error(403, "无权修改头像，请检查登陆状态");
        }
        String uploadPath = "/home/PeterAlbus/assets/blog/imgs/avatar/";
        String fileName = file.getOriginalFilename();
        String type = TypeUtil.getType(fileName);
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResultUtil.error(400, "用户ID不存在");
        }
        String newName;
        if (user.getUserPhone() != null) {
            newName = "avatar_" + user.getUserPhone() + "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
        } else if (user.getUserMail() != null) {
            newName = "avatar_" + user.getUserMail() + "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
        } else {
            return ResultUtil.error(500, "用户信息不完整");
        }
        if (TypeUtil.isImg(type)) {
            newName = newName +type;
            File dest = new File(uploadPath + newName);
            try {
                file.transferTo(dest);
                user.setUserAvatar("https://file.peteralbus.com/assets/blog/imgs/avatar/"+newName);
                userService.updateUser(user);
                return ResultUtil.success("https://file.peteralbus.com/assets/blog/imgs/avatar/"+newName);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return ResultUtil.error(500, "文件上传失败，请联系管理员");
            }
        }
        return ResultUtil.error(400, "文件类型不符合要求");
    }

    @RequestMapping("/changeUsername")
    public Result<?> changeUsername(Long userId, String username) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(userId)) {
            return ResultUtil.error(403, "无权修改用户名，请检查登陆状态");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResultUtil.error(400, "用户ID不存在");
        }
        user.setUserUsername(username);
        if (userService.updateUser(user) > 0) {
            return ResultUtil.success(null);
        }
        return ResultUtil.error(500, "用户名修改失败，请联系管理员");
    }

    @RequestMapping("/getUserById")
    public Result<?> getUserById(Long userId) {
        User user=userService.getUserById(userId);
        if(user==null){
            return ResultUtil.error(400,"用户ID不存在");
        }
        user.setUserPhone(null);
        user.setUserPassword(null);
        user.setUserMail(null);
        user.setUserSalt(null);
        return ResultUtil.success(user);
    }


}
