package com.swimcode.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 18:43
 */
@Slf4j
@Component
//userId表示占位符，用于区分不同的客户端用户
@ServerEndpoint("/api/websocket/{userId}")
public class WebSocketServer {
    /**
     * 当前在线连接数总数
     */
    public static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 用来存放每个客户端对应WebSocket对象
     */
    public static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @Getter
    private Session session;
    /**
     * 通过useId区分每一个用户的Session，用于实现点对点推送消息
     */
    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    //使用@Autowired一些列注解注入Bean时候，无法注入。
    //原因在于spring管理的都是单例（singleton），和 websocket （多对象,一个客户端相当于一个WebSocketServer对象）相冲突。
    //通过Spring上下文获取Bean
    private SocketSendTemplate socketSendTemplate;
    private SocketCallback socketCallback;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        webSocketSet.add(this);
        sessionMap.put(userId, session);
        //客户端个数加一
        addOnlineCount();
        try {
            if (socketSendTemplate == null) {
                socketSendTemplate = SpringUtil.getBean(SocketSendTemplate.class);
            }
            socketSendTemplate.sendToUser(userId, "连接websocket成功");
            log.info("有新的websocket客户端连接,用户id = {} 当前总客户端数量为:{}", userId, getOnlineCount());
        } catch (Exception e) {
            log.error("websocket建立连接失败");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！客户端用户id = {},当前总客户端数量={}", userId , getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @Autowired Session session, @PathParam("userId") String userId) {
        //应该注入回调
        log.info("收到来自客户端userId={}发送的信息,message ={} ", userId, message);
        if (socketCallback == null) {
            socketCallback = SpringUtil.getBean(SocketCallback.class);
        }
        socketCallback.invoke(message, session, userId);
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(@Autowired Session session, Throwable error) {
        log.error("发生错误");
    }


    /**
     * 返回当前所有的客户端总数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 客户端数量加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.incrementAndGet();
    }

    /**
     * 客户端数量减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.decrementAndGet();
    }

}
