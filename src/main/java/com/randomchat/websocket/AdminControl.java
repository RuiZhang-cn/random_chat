package com.randomchat.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ConcurrentHashMap;

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
 * @date 2019/12/9 -上午 9:56
 **/
@Controller
public class AdminControl {
    @Value("${adminCode}")
    private String adminCode;

    @GetMapping("/admin/{password}")
    public String toAdmin(@PathVariable String password, Model model) {
        if (adminCode.equals(password)) {
            ConcurrentHashMap<String, WebSocketServer> webSocketSet = WebSocketServer.webSocketSet;
            //连接详情
            model.addAttribute("webSocketSet", webSocketSet);
            return "admin";
        } else {
            return "index";
        }
    }
}
