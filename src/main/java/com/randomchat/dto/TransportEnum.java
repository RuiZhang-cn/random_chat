package com.randomchat.dto;

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
 * @date 2019/12/7 -下午 6:54
 **/
public enum TransportEnum {
    /**
     * 连接服务器成功
     *
     */
    TO_SERVICE_SUCCESS(TransportDto.SUCCESS(1001,"请求服务器成功")),
    /**
     * 配对成功
     */
    TO_LINK_SUCCESS(TransportDto.SUCCESS(1003, "配对成功")),
    /**
     * 对方已离线
     */
    TO_USERR_ERROR(TransportDto.SUCCESS(1005, "对方已离线")),
    /**
     * 连接服务器失败
     */
    TO_SERVICE_ERROR(TransportDto.ERROR(1002, "请求服务器失败")),
    /**
     * 连接服务器失败
     */
    SERVICE_ERROR(TransportDto.ERROR(1004, "服务器出错")),
    /**
     * 多次请求异常
     */
    MULTIPLE_REQUESTS_ERROR(TransportDto.ERROR(1006, "您同时打开了多个页面，这是禁止的，除非你想自己和自己聊天"));
    TransportDto transportDto;

    public TransportDto getTransportDto() {
        return transportDto;
    }

    public void setTransportDto(TransportDto transportDto) {
        this.transportDto = transportDto;
    }

    TransportEnum(TransportDto transportDto) {
        this.transportDto = transportDto;
    }
}
