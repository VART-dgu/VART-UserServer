package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.repository.RoomRepository;
import com.example.vrt.global.ping.WebSocketPingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuitRoomService {
    private final RoomRepository roomRepository;
    private final WebSocketPingService webSocketPingService;

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
            long[] latencies = new long[room.getParticipantIDs().size()];


            //각 participant에 ping 채우기
            Optional<String> newHostId = webSocketPingService.findFastestUser(room.getParticipantIDs());

            newHostId.ifPresentOrElse(hostId -> {
                room.setHostUserEndpoint(hostId);
                log.info("새 호스트로 설정된 사용자 ID: {}", hostId);
            },
                    ()-> {
                        log.warn("호스트를 선정할 수 있는 유효한 사용자 응답이 없습니다.");
                    });
        }

        //3. 새로운 호스트 broadcast

        return RoomQuitResponseDTO.builder().build();
    }
}