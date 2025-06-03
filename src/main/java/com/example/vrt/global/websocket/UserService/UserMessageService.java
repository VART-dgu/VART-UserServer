package com.example.vrt.global.websocket.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageService {
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;

    /**
     * 사용자 ID(userId)를 받아, 해당 사용자의 모든 열린 WebSocket 세션에 텍스트 메시지를 전송합니다.
     *
     * @param userId 텍스트를 받을 대상 사용자 ID
     * @param text   전송할 텍스트 내용
     * @throws IOException 메시지 전송 중 오류 발생 시 예외 던짐
     */
    public void sendTextToUser(String userId, String text) throws IOException {
        Set<String> sessionIds = userIdToSessionsMap.get(userId);
        if (sessionIds == null || sessionIds.isEmpty()) {
            log.warn("sendTextToUser: 열린 세션이 없습니다. userId={}", userId);
            return;
        }

        TextMessage textMessage = new TextMessage(text);
        for (String sessionId : sessionIds) {
            WebSocketSession wsSession = sessionMap.get(sessionId);
            if (wsSession == null) {
                log.warn("sendTextToUser: sessionMap에 세션이 없습니다. sessionId={}", sessionId);
                continue;
            }
            if (!wsSession.isOpen()) {
                log.warn("sendTextToUser: 이미 닫힌 세션입니다. sessionId={}", sessionId);
                continue;
            }
            try {
                wsSession.sendMessage(textMessage);
                log.info("sendTextToUser: userId={} sessionId={} 에 텍스트 전송: \"{}\"", userId, sessionId, text);
            } catch (IOException e) {
                log.error("sendTextToUser: 전송 중 오류 발생 userId={} sessionId={}", userId, sessionId, e);
                throw e;
            }
        }
    }
}
