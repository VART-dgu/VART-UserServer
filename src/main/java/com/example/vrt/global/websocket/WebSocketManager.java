package com.example.vrt.global.websocket;

import com.example.vrt.global.ping.WebSocketPingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketManager implements WebSocketMessageBrokerConfigurer {
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;
    private final ConcurrentHashMap<String, String> sessionIdToUserIdMap;
    private final WebSocketPingService webSocketPingService;

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(handler -> new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                // 세션 map에 저장
                sessionMap.put(session.getId(), session);
                super.afterConnectionEstablished(session);
                log.info("WebSocket 연결 성립: sessionId={}", session.getId());
            }
            
            //Ping 응답 메시지 처리
            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                if (message instanceof PongMessage) {
                    String sessionId = session.getId();
                    webSocketPingService.onPongReceived(sessionId);
                    return;
                }
                //나머지 메시지는 원래 로직대로 처리
                super.handleMessage(session, message);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                // 세션 map에서 제거
                sessionMap.remove(session.getId());
                super.afterConnectionClosed(session, status);
                log.info("WebSocket 연결 종료: sessionId={}", session.getId());

                // STOMP DISCONNECT를 못 받았을 때도 userId 매핑 정리
                String userId = sessionIdToUserIdMap.remove(session.getId());
                
                if (userId != null) {
                    Set<String> sessions = userIdToSessionsMap.get(userId);
                    if (sessions != null) {
                        sessions.remove(session.getId());
                        if (sessions.isEmpty()) {
                            userIdToSessionsMap.remove(userId);
                        }
                    }
                    log.info("sessionId→userId 매핑 제거: sessionId={}, userId={}", session.getId(), userId);
                }
            }
        });
    }
}
