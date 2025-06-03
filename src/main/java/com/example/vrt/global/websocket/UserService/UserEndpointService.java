package com.example.vrt.global.websocket.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserEndpointService {
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;

    /**
     * 사용자 ID(userId)를 받아, 그 사용자가 열어 놓은 세션 중 하나의 엔드포인트(IP:포트)를 Optional로 반환합니다.
     * 가장 먼저 발견되는 “열려 있는 첫 번째 세션”을 기준으로 합니다.
     *
     * @param userId 사용자 ID
     * @return Optional<String> (예: "192.168.0.10:53624"), 없으면 Optional.empty()
     */
    public Optional<String> findAnyEndpointByUserId(String userId) {
        Set<String> sessionIds = userIdToSessionsMap.get(userId);
        if (sessionIds == null || sessionIds.isEmpty()) {
            return Optional.empty();
        }

        for (String sessionId : sessionIds) {
            WebSocketSession wsSession = sessionMap.get(sessionId);
            if (wsSession != null && wsSession.isOpen()) {
                InetSocketAddress remote = wsSession.getRemoteAddress();
                if (remote != null) {
                    String endpoint = remote.getAddress().getHostAddress() + ":" + remote.getPort();
                    return Optional.of(endpoint);
                }
            }
        }
        return Optional.empty();
    }
}
