package com.randomchat.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
 * @date 2019/12/7 -下午 4:57
 **/
@Controller
public class WebSocketControl {
@RequestMapping(value = {"/","index"})
    public String to(){
    return "index";
}
@RequestMapping(value = {"lat"})
    public String tolet(){
    return "lat";
}
}
