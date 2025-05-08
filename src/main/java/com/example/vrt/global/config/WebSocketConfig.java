package com.example.vrt.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //client가 구독하는 경로의 prefix
        config.enableSimpleBroker("/topic");
        //client로부터의 메시지 수신 경로 prefix
        config.setApplicationDestinationPrefixes("/app");
    }

    //STOMP websocket endpoint : /ws
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //client에 websocket 연결에 사용할 엔드포인트 제공
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*");
    }

    @Bean //열려있는 세션을 보관할 Map
    public ConcurrentHashMap<String, WebSocketSession> sessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(handler -> new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                // 세션이 열릴 때 map에 저장
                sessionMap().put(session.getId(), session);
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // 세션이 닫힐 때 map에서 제거
                sessionMap().remove(session.getId());
                super.afterConnectionClosed(session, closeStatus);
            }
        });
    }
}