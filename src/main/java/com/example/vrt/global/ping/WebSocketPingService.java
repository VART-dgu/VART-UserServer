package com.example.vrt.global.ping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 사용자 ID 리스트를 받아 해당 사용자의 모든 열린 WebSocketSession에 Ping을 전송합니다.
 * sessionMap: sessionId → WebSocketSession
 * userIdToSessionsMap: userId → Set<sessionId>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketPingService {
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;
    private final ConcurrentHashMap<String, String> sessionIdToUserIdMap;

    // 1) sessionId → 마지막으로 Ping을 보낸 시각 (ms)
    private final ConcurrentHashMap<String, Long> pingTimes = new ConcurrentHashMap<>();

    // 2) sessionId → 마지막으로 계산된 왕복 지연(latency) (ms)
    private final ConcurrentHashMap<String, Long> latencies = new ConcurrentHashMap<>();

    /**
     * 주어진 사용자 ID 리스트(participantIds)에 대해, 해당 사용자의 모든 세션으로 Ping을 보냅니다.
     */
    public void onPongReceived(String sessionId) {
        Long sendTime = pingTimes.remove(sessionId);
        if (sendTime == null) {
            // Ping을 보낸 적이 없거나 이미 처리된 경우
            return;
        }
        long now = System.currentTimeMillis();
        long latency = now - sendTime;
        latencies.put(sessionId, latency);
        log.debug("Pong received for sessionId={} , latency={}ms", sessionId, latency);
    }

    /**
     * 지정된 사용자 목록(participantIds)에 Ping을 보내고, 잠시 기다렸다가
     * 가장 빠른 응답 속도를 보인 userId를 Optional로 반환합니다.
     *
     * 만약 활성 세션이 하나도 없다면 Optional.empty()를 반환합니다.
     */
    public Optional<String> findFastestUser(List<String> participantIds) {
        // 0) 기존 latency 데이터 초기화
        latencies.clear();
        pingTimes.clear();

        // 1) participantIds 중에 연결된 세션이 있으면 모든 세션에 Ping 전송
        byte[] payload = "ping".getBytes();
        PingMessage pingMessage = new PingMessage(ByteBuffer.wrap(payload));

        for (String userId : participantIds) {
            Set<String> sessions = userIdToSessionsMap.get(userId);
            if (sessions == null || sessions.isEmpty()) {
                log.debug("No active sessions for userId={}", userId);
                continue;
            }
            for (String sessionId : sessions) {
                WebSocketSession wsSession = sessionMap.get(sessionId);
                if (wsSession == null || !wsSession.isOpen()) {
                    continue;
                }
                try {
                    long now = System.currentTimeMillis();
                    // 보낸 시각을 기록
                    pingTimes.put(sessionId, now);
                    wsSession.sendMessage(pingMessage);
                    log.debug("Sent Ping to sessionId={} (userId={}) at {}", sessionId, userId, now);
                } catch (Exception ex) {
                    log.error("Failed to send Ping: sessionId={}, userId={}", sessionId, userId, ex);
                }
            }
        }

        // 2) Pong이 돌아올 시간을 잠시 기다림 (예: 200ms)
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 3) latencies 맵에 쌓인 값들을 바탕으로 userId별 최소 latency 계산
        Map<String, Long> bestLatencyPerUser = new HashMap<>();
        for (Map.Entry<String, Long> entry : latencies.entrySet()) {
            String sessionId = entry.getKey();
            long latency = entry.getValue();
            String userId = sessionIdToUserIdMap.get(sessionId);
            if (userId == null) {
                continue;
            }
            bestLatencyPerUser.merge(userId, latency, Math::min);
        }

        // 4) 가장 낮은 latency를 가진 userId를 찾음
        return bestLatencyPerUser.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
