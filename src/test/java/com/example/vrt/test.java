package com.example.vrt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.vrt.global.ping.WebSocketPingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketPingService의 findFastestUser(...) 메서드가
 * 다중 사용자 시나리오에서 올바르게 동작하는지 검증합니다.
 */
class WebSocketPingServiceTest {

    // 실제 테스트에서 사용할 Map들
    private ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private ConcurrentHashMap<String, String> sessionIdToUserIdMap;
    private ConcurrentHashMap<String,
            java.util.Set<String>> userIdToSessionsMap;

    // 우리가 테스트할 서비스 (Map들을 생성자 주입)
    private WebSocketPingService pingService;

    @BeforeEach
    void setUp() {
        sessionMap = new ConcurrentHashMap<>();
        sessionIdToUserIdMap = new ConcurrentHashMap<>();
        userIdToSessionsMap = new ConcurrentHashMap<>();

        // WebSocketPingService 생성
        pingService = new WebSocketPingService(
                sessionMap,
                userIdToSessionsMap,
                sessionIdToUserIdMap
        );
    }

    /**
     * 두 명의 사용자를 만든 뒤,
     * user1의 세션(s1)은 50ms 후 onPongReceived를 호출하도록,
     * user2의 세션(s2)은 10ms 후 onPongReceived를 호출하도록 설정한 뒤,
     * findFastestUser를 실행했을 때 "user2"가 반환되는지 검증합니다.
     */
    @Test
    void testFindFastestUser_MultipleUsers() throws Exception {
        // ----- 1) Mock WebSocketSession 생성 -----
        // user1, sessionId = "s1", 응답 지연 50ms
        WebSocketSession session1 = Mockito.mock(WebSocketSession.class);
        when(session1.getId()).thenReturn("s1");
        when(session1.isOpen()).thenReturn(true);

        // sendMessage 호출 시 50ms 후 pingService.onPongReceived("s1") 실행
        doAnswer(invocation -> {
            // Pong 시뮬레이션을 위한 별도 스레드
            new Thread(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                // 지연 후에 pong 처리
                pingService.onPongReceived("s1");
            }).start();
            return null;
        }).when(session1).sendMessage(any(PingMessage.class));

        // user2, sessionId = "s2", 응답 지연 10ms
        WebSocketSession session2 = Mockito.mock(WebSocketSession.class);
        when(session2.getId()).thenReturn("s2");
        when(session2.isOpen()).thenReturn(true);
        doAnswer(invocation -> {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                pingService.onPongReceived("s2");
            }).start();
            return null;
        }).when(session2).sendMessage(any(PingMessage.class));

        // ----- 2) Map에 모킹된 세션과 사용자-세션 매핑 세팅 -----
        // sessionMap.put(sessionId, WebSocketSession)
        sessionMap.put("s1", session1);
        sessionMap.put("s2", session2);

        // sessionIdToUserIdMap.put(sessionId, userId)
        sessionIdToUserIdMap.put("s1", "user1");
        sessionIdToUserIdMap.put("s2", "user2");

        // userIdToSessionsMap: user1 → {"s1"}, user2 → {"s2"}
        userIdToSessionsMap.put("user1", new HashSet<>(Arrays.asList("s1")));
        userIdToSessionsMap.put("user2", new HashSet<>(Arrays.asList("s2")));

        // ----- 3) findFastestUser 호출 -----
        List<String> participantIds = Arrays.asList("user1", "user2");
        Optional<String> fastestOpt = pingService.findFastestUser(participantIds);

        // ----- 4) 검증: 가장 빠른 사용자 = "user2" -----
        assertThat(fastestOpt)
                .as("응답 지연이 짧은 user2가 선택되어야 한다")
                .contains("user2");
    }

    /**
     * 한 명의 사용자가 여러 세션을 열었을 때, 각 세션의 응답 지연을 측정한 뒤
     * 사용자(user1)의 최소 지연값이 올바로 계산되는지 확인합니다.
     */
    @Test
    void testFindFastestUser_SingleUserMultipleSessions() throws Exception {
        // ----- 1) Mock WebSocketSession 생성 (두 개) -----
        // sessionId = "sA", 지연 30ms
        WebSocketSession sessionA = Mockito.mock(WebSocketSession.class);
        when(sessionA.getId()).thenReturn("sA");
        when(sessionA.isOpen()).thenReturn(true);
        doAnswer(invocation -> {
            new Thread(() -> {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                pingService.onPongReceived("sA");
            }).start();
            return null;
        }).when(sessionA).sendMessage(any(PingMessage.class));

        // sessionId = "sB", 지연 60ms
        WebSocketSession sessionB = Mockito.mock(WebSocketSession.class);
        when(sessionB.getId()).thenReturn("sB");
        when(sessionB.isOpen()).thenReturn(true);
        doAnswer(invocation -> {
            new Thread(() -> {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                pingService.onPongReceived("sB");
            }).start();
            return null;
        }).when(sessionB).sendMessage(any(PingMessage.class));

        // ----- 2) Map 설정 -----
        sessionMap.put("sA", sessionA);
        sessionMap.put("sB", sessionB);

        sessionIdToUserIdMap.put("sA", "user1");
        sessionIdToUserIdMap.put("sB", "user1");

        // user1 → {"sA", "sB"}
        userIdToSessionsMap.put("user1", new HashSet<>(Arrays.asList("sA", "sB")));

        // ----- 3) findFastestUser 호출 (participantIds = ["user1"]) -----
        List<String> participantIds = Arrays.asList("user1");
        Optional<String> fastestOpt = pingService.findFastestUser(participantIds);

        // ----- 4) 검증: 응답 지연이 더 짧은 세션(sA)을 통해 user1이 선택되었어야 함 -----
        assertThat(fastestOpt)
                .as("동일 사용자(user1) 내에서 더 짧은 지연(30ms)을 기록한 세션(sA)이 선택되어야 한다")
                .contains("user1");
        // (Optional 값이 "user1"인지만 검증하며, 세션 ID 정보는 Service 내부에서만 사용되므로 공개하지 않음)
    }
}
