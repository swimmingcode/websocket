package com.swimcode.data;

import com.alibaba.fastjson.JSONObject;
import com.swimcode.config.SocketSendTemplate;
import com.swimcode.config.WebSocketNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/16 20:32
 */
@Component
public class Task {



    @Autowired
    private SocketSendTemplate socketSendTemplate;


    @Scheduled(cron = "0/1 * * * * * ")
    public void run() {
        WebSocketNotice build = WebSocketNotice.builder().type("Report").build();
        socketSendTemplate.sendToAllUser(JSONObject.toJSONString(build));
    }
}
