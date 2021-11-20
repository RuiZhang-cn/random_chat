package com.randomchat.websocket;

/**
 * 所属项目:random_chat
 *
 * @author rui10038 邮箱：2450782689@qq.com
 * @version 1.0
 * //                .-~~~~~~~~~-._       _.-~~~~~~~~~-.
 * //            __.'              ~.   .~              `.__
 * //          .'//    JAVA无涯      \./     回头是岸     \\`.
 * //        .'//                     |                     \\`.
 * //      .'// .-~"""""""~~~~-._     |     _,-~~~~"""""""~-. \\`.
 * //    .'//.-"                 `-.  |  .-'                 "-.\\`.
 * //  .'//______.============-..   \ | /   ..-============.______\\`.
 * //.'______________________________\|/______________________________`.
 * @date 2019/12/7 -下午 9:15
 **/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @author :ZHANGPENGFEI
 * @create 2018-07-11 13:52
 **/
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}