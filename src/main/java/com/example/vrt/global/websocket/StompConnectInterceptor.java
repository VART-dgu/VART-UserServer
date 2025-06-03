package com.example.vrt.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * STOMP CONNECT / DISCONNECT 프레임을 가로채 user-id를 추출하여
 * sessionIdToUserIdMap, userIdToSessionsMap을 관리한다.
 */
@Component
@RequiredArgsConstructor
public class StompConnectInterceptor implements ChannelInterceptor {

    private final ConcurrentHashMap<String, String> sessionIdToUserIdMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 1) STOMP CONNECT 프레임 처리: client가 CONNECT 요청 보낼 때마다 호출
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getFirstNativeHeader("user-id");
            if (userId != null && !userId.isEmpty()) {
                String sessionId = accessor.getSessionId();

                // ① sessionId → userId 맵에 저장
                sessionIdToUserIdMap.put(sessionId, userId);

                // ② userId → Set<sessionId> 맵에 저장 (최초 한 번, 없으면 새 Set 생성)
                userIdToSessionsMap.compute(userId, (key, set) -> {
                    if (set == null) {
                        set = ConcurrentHashMap.newKeySet();
                    }
                    set.add(sessionId);
                    return set;
                });
            }
        }

        // 2) STOMP DISCONNECT 프레임 처리: client가 명시적으로 DISCONNECT 보낼 때
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            String userId = sessionIdToUserIdMap.remove(sessionId);
            if (userId != null) {
                Set<String> sessions = userIdToSessionsMap.get(userId);
                if (sessions != null) {
                    sessions.remove(sessionId);
                    if (sessions.isEmpty()) {
                        userIdToSessionsMap.remove(userId);
                    }
                }
            }
        }

        return message;
    }
}
