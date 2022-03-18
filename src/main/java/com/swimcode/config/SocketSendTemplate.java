package com.swimcode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

import java.io.IOException;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 21:06
 */
@Slf4j
@Component
public class SocketSendTemplate implements SocketEvent {

    public SocketSendTemplate() {}

    @Override
    public void sendToUser(String userId, String message) {
        Session session = WebSocketServer.sessionMap.get(userId);
        if (session == null) {
            log.error("错误，当前用户不在连接中");
            return;
        }
        sendMessage(session, message);
        log.info("给id= {}的用户推送消息,message = {}", userId, message);

    }

    @Override
    public void sendToAllUser(String message) {
        log.info("群发推送消息内容为:" + message);
        try {
            for (WebSocketServer item : WebSocketServer.webSocketSet) {
                sendMessage(item.getSession(), message);
            }
        } catch (Exception e) {
            log.error("推送消息失败");
        }
    }

    /**
     * 服务器主动推送消息给客户端
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("服务端发送消息失败");
        }
    }
}
