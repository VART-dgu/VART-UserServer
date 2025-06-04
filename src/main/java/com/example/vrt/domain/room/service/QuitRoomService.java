package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.repository.RoomRepository;
import com.example.vrt.global.ping.WebSocketPingService;
import com.example.vrt.global.websocket.UserService.UserEndpointService;
import com.example.vrt.global.websocket.UserService.UserMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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
    private final UserEndpointService userEndpointService;
    private final UserMessageService userMessageService;

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
        if(requestDTO.getIsHost()) {
            //ping 테스트로 host ID 찾기
            Optional<String> newHostId = webSocketPingService.findFastestUser(room.getParticipantIDs());

            newHostId.ifPresentOrElse(hostId -> {
                //hostId 설정
                room.setHostUserId(hostId);
                //host ID 기반으로 host endpoint 검색
                Optional<String> hostEndpoint = userEndpointService.findAnyEndpointByUserId(hostId);

                hostEndpoint.ifPresentOrElse(endpoint -> {
                    //host Endpoint 설정
                    room.setHostUserEndpoint(endpoint);

                    //방의 모든 사용자에게 host endpoint 전송
                    for (String userId : room.getParticipantIDs()) {
                        try {
                            userMessageService.sendTextToUser(userId, endpoint);
                        } catch (IOException e) {
                            log.error("호스트 ID={} 세션으로 메시지 전송 중 오류 발생", hostId, e);
                        }
                    }
                }, () -> {
                    log.warn("주어진 hostId={} 로 열린 WebSocket 세션을 찾지 못했습니다.", hostId);
                });
            }, () -> {
                log.warn("Host를 발견하지 못했습니다.");
            });

        }

        return RoomQuitResponseDTO.builder()
                .roomId(room.getId())
                .galleryId(room.getGallery().getId())
                .hostUserEndpoint(room.getHostUserEndpoint())
                .isHost(true)
                .mapFileURL(room.getMapFileURL())
                .build();
    }
}