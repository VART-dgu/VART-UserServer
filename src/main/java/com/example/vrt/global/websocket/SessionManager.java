package com.example.vrt.global.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionManager {
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    
    //세션을 종료
    public void disconnectSession(String sessionId) {
        WebSocketSession session = sessionMap.remove(sessionId);
        
        if (session != null && session.isOpen()) {
            try {
                session.close(CloseStatus.NORMAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
