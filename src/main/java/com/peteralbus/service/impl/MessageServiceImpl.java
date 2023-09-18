package com.peteralbus.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peteralbus.domain.Message;
import com.peteralbus.mapper.MessageMapper;
import com.peteralbus.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    @Override
    public List<Message> queryMessageByTargetId(Long userId) {
        QueryWrapper<Message> messageQueryWrapper=new QueryWrapper<>();
        messageQueryWrapper.eq("target_id",userId);
        return messageMapper.selectList(messageQueryWrapper);
    }

    @Override
    public List<Message> queryMessageBySenderId(Long userId) {
        QueryWrapper<Message> messageQueryWrapper=new QueryWrapper<>();
        messageQueryWrapper.eq("sender_id",userId);
        return messageMapper.selectList(messageQueryWrapper);
    }

    @Override
    public Message queryMessageById(Long messageId) {
        return messageMapper.selectById(messageId);
    }

    @Override
    public int addMessage(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public int deleteMessage(Long messageId) {
        return messageMapper.deleteById(messageId);
    }

    @Override
    public int readMessage(Long messageId) {
        Message message = messageMapper.selectById(messageId);
        Long userId = StpUtil.getLoginIdAsLong();
        if (!message.getTargetId().equals(userId)) {
            return 0;
        }
        message.setIsRead(true);
        return messageMapper.updateById(message);
    }

    @Override
    public Long getUnreadMessageCount(Long userId) {
        QueryWrapper<Message> messageQueryWrapper=new QueryWrapper<>();
        messageQueryWrapper.eq("target_id",userId).eq("is_read",false);
        return messageMapper.selectCount(messageQueryWrapper);
    }


}
