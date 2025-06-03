package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.repository.RoomRepository;
import com.example.vrt.global.ping.WebSocketPingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuitRoomService {
    private final RoomRepository roomRepository;
    private final WebSocketPingService webSocketPingService;
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap;
    private final ConcurrentHashMap<String, String> sessionIdToUserIdMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToSessionsMap;

    public RoomQuitResponseDTO quitRoom(RoomQuitRequestDTO requestDTO) {
        //1. 사용자 제거

        //방 조회
        Room room = roomRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Room을 찾을 수 없음"));

        //방 사용자에서 사용자 제거
        List<String> participantIDs = room.getParticipantIDs();

        for(int i = 0; i < participantIDs.size(); i++){
            if(participantIDs.get(i).equals(requestDTO.getUserId())){
                room.removeParticipant(requestDTO.getUserId());
                break;
            }
        }

        //2. 호스트라면 모든 사용자에게 핑 테스트, 가장 낮은 핑을 새로운 호스트로 선정, 저장
        if(requestDTO.getIsHost()){
            //각 participant에 ping 채우기
            Optional<String> newHostId = webSocketPingService.findFastestUser(room.getParticipantIDs());

            newHostId.ifPresentOrElse(hostId -> {
                Set<String> sessionIds = userIdToSessionsMap.get(hostId);

                if (sessionIds == null || sessionIds.isEmpty()) {
                    log.warn("호스트로 선정되었으나 열린 WebSocket 세션이 없습니다. hostId={}", hostId);
                    return;
                }

                for (String sessionId : sessionIds) {
                    WebSocketSession session = sessionMap.get(sessionId);

                    if (session == null || !session.isOpen()) {
                        continue;
                    }

                    InetSocketAddress remote = session.getRemoteAddress();

                    if (remote != null) {
                        String endpoint = remote.getAddress().getHostAddress() + ":" + remote.getPort();
                        log.info("호스트의(사용자 {})의 엔드포인트: {}", hostId, endpoint);
                    } else {
                        log.info("호스트(사용자 {})의 WebSocketSession은 있지만 엔드포인트를 얻지 못했습니다.", hostId);
                    }
                }
            },
                    () -> {
                log.warn("호스트를 설정하지 못했습니다.");
                    });

        //3. 새로운 호스트 broadcast

        return RoomQuitResponseDTO.builder()
                .roomId(room.getId())
                .galleryId(room.getGallery().getGalleryId())
                .hostUserEndpoint(room.getHostUserEndpoint())
                .isHost(true)
                .mapFileURL(room.getMapFileURL())
                .build();
    }
}