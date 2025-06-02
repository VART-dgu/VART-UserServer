package com.example.vrt.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final SessionRepository sessionRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();

        sessionRepository.registerSession(sessionId, session);

        log.info("새로운 WebSocket 연결! sessionId = " + sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        //연결이 끊기면 session Id를 얻어서 세션 제거
        String sessionId = session.getId();
        sessionRepository.removeSession(sessionId);

        log.info("WebSocket 연결 종료. sessionId = " + sessionId);
    }
}
