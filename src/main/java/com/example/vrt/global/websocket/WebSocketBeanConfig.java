package com.example.vrt.global.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WebSocketBeanConfig {
    @Bean
    public ConcurrentHashMap<String, WebSocketSession> sessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, Set<String>> userIdToSessionsMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, String> sessionIdToUserIdMap() {
        return new ConcurrentHashMap<>();
    }
}
