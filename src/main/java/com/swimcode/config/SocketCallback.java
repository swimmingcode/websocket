package com.swimcode.config;


import javax.websocket.Session;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 21:18
 */
public interface  SocketCallback {
    void invoke(String message, Session session,String userId);
}
