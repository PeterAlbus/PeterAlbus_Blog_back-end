package com.peteralbus.util;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.domain.Blog;
import com.peteralbus.domain.Comment;
import com.peteralbus.domain.Message;
import com.peteralbus.domain.User;
import com.peteralbus.service.BlogService;
import com.peteralbus.service.MessageService;
import com.peteralbus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageUtil {
    private final MessageService messageService;
    private final UserService userService;
    private final BlogService blogService;
    public static final Long OWNER_ID = 1507660309008289794L;

    public void sendMessage(Long targetId, String content, String title, Boolean system) {
        content = "<span>" + content + "</span>";
        Message message = new Message();
        if (system) {
            message.setSenderId(-1L);
            message.setSenderName("系统");
        } else {
            if (!StpUtil.isLogin()) {
                return;
            }
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userService.getUserById(userId);
            message.setSenderId(userId);
            message.setSenderName(user.getUserUsername());
        }
        message.setTargetId(targetId);
        message.setMessageContent(content);
        message.setMessageTitle(title);
        message.setIsRead(false);
        message.setGmtCreate(LocalDateTime.now());
        messageService.addMessage(message);
    }

    public void sendReplyMessage(Comment comment, Long targetId, Long blogId) {
        Long senderId = comment.getCommentUserId();
        User sender = userService.getUserById(senderId);
        Blog blog = blogService.queryById(blogId);
        String messageContent;
        String messageTitle;
        if (comment.getCommentTarget().equals(1)) {
            messageContent = "您的博客《<a href='" + "/#/blog?id=" + blogId
                    + "'>" + blog.getBlogTitle() + "</a>》收到了来自" + sender.getUserUsername()
                    + "的一条评论";
            messageTitle = "评论提醒";
        } else {
            messageContent = "您在博客《<a href='" + "/#/blog?id=" + blogId
                    + "'>" + blog.getBlogTitle() + "</a>》下的评论收到了来自" + sender.getUserUsername()
                    + "的一条回复";
            messageTitle = "回复提醒";
        }
        sendMessage(targetId, messageContent, messageTitle, true);
    }
}
