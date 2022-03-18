package com.swimcode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 21:23
 */
@Component
public class SocketCallbackImpl implements SocketCallback {

    public SocketCallbackImpl(SocketSendTemplate socketSendTemplate) {
        this.socketSendTemplate = socketSendTemplate;
    }

    @Autowired
    private SocketSendTemplate socketSendTemplate;

    @Override
    public void invoke(String message, Session session, String userId) {
        //响应客户端
        socketSendTemplate.sendToUser(userId, "已接收的你发送的消息" + message);
    }
}
