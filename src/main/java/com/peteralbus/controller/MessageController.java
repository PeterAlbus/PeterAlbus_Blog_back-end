package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.domain.Message;
import com.peteralbus.domain.Result;
import com.peteralbus.service.MessageService;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {
    private final MessageService messageService;

    @RequestMapping("/getUnreadMessageCount")
    public Result<?> getUnreadMessageCount() {
        if (!StpUtil.isLogin()) {
            return ResultUtil.error(400,"未登录");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        return ResultUtil.success(messageService.getUnreadMessageCount(userId));
    }

    @RequestMapping("/readMessage")
    public Result<?> readMessage(Long messageId) {
        if (messageService.readMessage(messageId) > 0) {
            return ResultUtil.success(null);
        }
        return ResultUtil.error(500,"设为已读失败");
    }
}
