package com.peteralbus.service;

import com.peteralbus.domain.Message;

import java.util.List;

public interface MessageService {
    List<Message> queryMessageByTargetId(Long userId);
    List<Message> queryMessageBySenderId(Long userId);
    Message queryMessageById(Long messageId);
    int addMessage(Message message);
    int deleteMessage(Long messageId);
    int readMessage(Long messageId);
    Long getUnreadMessageCount(Long userId);
}
