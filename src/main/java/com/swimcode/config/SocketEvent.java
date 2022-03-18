package com.swimcode.config;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 21:02
 */
public interface SocketEvent {
    /**
     * 发送给特定用户
     *
     * @param userId
     * @param message
     */
    void sendToUser(String userId, String message);

    /**
     * 发送给送有用户
     *
     * @param message
     */
    void sendToAllUser(String message);
}
