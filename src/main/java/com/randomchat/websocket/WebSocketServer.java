package com.randomchat.websocket;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.randomchat.dto.TransportDto;
import com.randomchat.dto.TransportEnum;
import com.randomchat.util.IpUtil;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
 * @date 2019/12/7 -下午 4:18
 **/
@ServerEndpoint(value = "/websocket")
@Component
@Data
public class WebSocketServer {
    //所有连接
    public static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //所有在配对的ID
    private  static List<String> webSocketLiveList = new CopyOnWriteArrayList<>();
     //    自己的id标识
    private String id = "";
    //连接对象的id标识
    private String toUser = "";

    private static Logger log = LogManager.getLogger(WebSocketServer.class);
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(3600000);
        this.session = session;
        //获取用户ip
        String ip = String.valueOf(IpUtil.getRemoteAddress(session));
        String[] split = ip.split(":");
        if (split.length > 1) {
            ip = split[0];
        }
        this.id = ip;
        //加入set中
        WebSocketServer put = webSocketSet.put(this.id, this);
        //如果已经在队里，就不去找对象
        if (put == null) {
            try {
                if (pair()) {
                    sendMessage(TransportEnum.TO_LINK_SUCCESS.getTransportDto());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                sendMessage(TransportEnum.MULTIPLE_REQUESTS_ERROR.getTransportDto());
                webSocketSet.remove(this.id); //从set中删除
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        log.info("用户" + this.id + "加入！当前在线人数为" + webSocketSet.size());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WebSocketServer UserId = webSocketSet.get(toUser);
     webSocketLiveList.remove(this.id);
        if (UserId!=null){
            try {
                sendtoUser(session, "对方已离开", toUser, 1005);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        webSocketSet.remove(this.id); //从set中删除
        log.info("有一连接关闭！当前在线人数为" + webSocketSet.size()+"当前在匹配的人数为:"+webSocketLiveList.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        try {
            WebSocketServer.sendtoUser(session, message, toUser,1000 );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
        try {
            SendSelf(session, TransportEnum.SERVICE_ERROR.getTransportDto());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(TransportDto message) throws IOException {
        SendSelf(this.session,message);
    }

    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @param message
     * @param sendUserId
     * @param code
     * @throws IOException
     */
    public static void sendtoUser(Session session, String message, String sendUserId, Integer code) throws IOException {
        WebSocketServer UserId = webSocketSet.get(sendUserId);
        if (UserId != null) {
            if (message.startsWith("img:")){
                UserId.sendMessage(TransportDto.SUCCESSIMG(code, message.substring(message.indexOf(":")+1)));
            }else if (message.startsWith("video:")){
                UserId.sendMessage(TransportDto.SUCCESSVIDEO(code, message.substring(message.indexOf(":")+1)));
            }else {
                UserId.sendMessage(TransportDto.SUCCESS(code, message));
            }
        }else {
            SendSelf(session, TransportEnum.TO_USERR_ERROR.getTransportDto());
        }
    }

    private static void SendSelf(Session session, TransportDto transportDto) throws IOException {
        session.getBasicRemote().sendText(JSON.toJSONString(transportDto));
    }

    /**
     * 通知除了自己之外的所有人
     */
    private void sendOnlineCount( String message) {
        for (String key : webSocketSet.keySet()) {
            try {
                if (key.equals(id)){
                    continue;
                }
                webSocketSet.get(key).sendMessage(TransportDto.SUCCESS(1000, message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送信息给所有人
     * @param message
     * @throws IOException
     */
    public void sendtoAll(String message) throws IOException {
        for (String key : webSocketSet.keySet()) {
            try {
                webSocketSet.get(key).sendMessage(TransportDto.SUCCESS(1000, message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized boolean pair(){
        if (webSocketLiveList.size()>0){
            Random ra =new Random();
            int nextInt = ra.nextInt(webSocketLiveList.size())+1;
            toUser = webSocketLiveList.get(nextInt-1);
            System.out.println(toUser);
            try {
                WebSocketServer UserId = webSocketSet.get(toUser);
                UserId.setToUser(id);
                sendtoUser(session, "配对成功", toUser,1003 );
            } catch (IOException e) {
                e.printStackTrace();
            }
            webSocketLiveList.remove(nextInt-1);
            return true;
        }
        webSocketLiveList.add(id);
        return false;
    }

}
