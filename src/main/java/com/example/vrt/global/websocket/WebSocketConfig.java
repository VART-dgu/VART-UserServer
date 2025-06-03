package com.example.vrt.global.websocket;

import com.example.vrt.global.config.StompConnectInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompConnectInterceptor stompConnectInterceptor;

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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(stompConnectInterceptor);
    }
}