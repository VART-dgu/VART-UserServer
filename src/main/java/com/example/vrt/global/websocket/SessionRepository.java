package com.example.vrt.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionRepository {
    //sessionId 기반으로 WebSocketSession 객체 저장
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;

    public void registerSession(String sessionId, WebSocketSession session){
        sessionMap.put(sessionId, session);
    }

    //세션을 종료
    public void removeSession(String sessionId) {
        try {
            WebSocketSession session = sessionMap.remove(sessionId);
        } catch (Exception e) {
            log.error("removeSession: sessionMap.remove('{}') 실행 중 예외 발생", sessionId, e);
        }
    }

    //사용자의 endpoint 가져오기
    public String getClientEndpoint(String sessionId){
        WebSocketSession session = sessionMap.get(sessionId);
        if(session == null || !session.isOpen()){
            return null;
        }

        InetSocketAddress address = session.getRemoteAddress();
        return (address != null) ? address.toString() : null;
    }
}
