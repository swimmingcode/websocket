package com.swimcode.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: #请描述当前类功能#
 * @Author：youjiancheng
 * @Date: 2022/3/17 9:21
 */
@Data
@Builder
public class WebSocketNotice {
    private String type;
    private JSONObject content;
}
