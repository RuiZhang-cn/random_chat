package com.randomchat;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RandomChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomChatApplication.class, args);
    }

    @Bean
    public AES aes(){
        return new AES(Mode.CTS, Padding.PKCS5Padding, "lizhou205000!@#$%^&".getBytes(), "0102030405060708".getBytes());
    }

}
