package com.randomchat.config;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by Rui
 * @Description
 * @Date 2022/09/20 下午 9:30
 */
@Configuration
public class RandomChatConfig {
    @Value("${aesKey}")
    private String aesKey;
    @Value("${aesIv}")
    private String aesIv;
    @Bean
    public AES aes(){
        //由于AES的秘钥规定是16位秘钥
        return new AES(Mode.CTS, Padding.PKCS5Padding, aesKey.getBytes(), aesIv.getBytes());
    }
}
