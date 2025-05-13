package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.*;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.mapper.RoomMapper;
import com.example.vrt.domain.room.repository.RoomRepository;
import com.example.vrt.global.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final PingService pingService;

    @Override
    public RoomJoinResponseDTO joinRoom(RoomJoinRequestDTO requestDTO) {
        // 1. roomId로 방 조회
        Room room = roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 2. 인원수 제한 확인
        if (room.getCurrentParticipants() >= room.getMaxParticipants()) {
            throw new IllegalStateException("이 방은 이미 가득 찼습니다.");
        }

        // 3. 첫 번째 유저일 경우 호스트 지정
        boolean isHost = room.getCurrentParticipants() == 0;
        if (isHost) {
            room.setHostUserId(requestDTO.getUserId());
            room.setHostUserEndpoint(requestDTO.getUserEndpoint());

            // TODO: 리슨서버에게 "방 열기" 요청 전송 (ex. 메시지큐, 내부 HTTP 요청 등)
        }

        // 4. 참가자 추가
        room.addParticipant(requestDTO.getUserId());

        // 5. 저장
        roomRepository.save(room);

        // 6. 응답 DTO 생성 및 반환
        return new RoomJoinResponseDTO(
                room.getId(),
                room.getGallery().getId(),
                room.getHostUserEndpoint(),
                isHost,
                room.getMapFileURL(),
                room.getCurrentParticipants(),
                room.getMaxParticipants()
        );
    }

//    public RoomQuitResponseDTO quitRoom(RoomQuitRequestDTO requestDTO) {
//        //1. 사용자 제거
//        RoomDTO roomDTO = RoomMapper.toRoomDTO(roomRepository.findById(requestDTO.getRoomId())
//                .orElseThrow(() -> new NoSuchElementException("Room을 찾을 수 없음")));
//
//        //3. 호스트라면 모든 사용자에게 핑 테스트, 가장 낮은 핑을 새로운 호스트로 선정
//        if(requestDTO.getIsHost()){
//            String newHostId = null;
//            long bestLatency = Long.MAX_VALUE;
//            for(String s : roomDTO.getParticipantIDs()){
//                long latency = pingService.ping(s);
//                if(latency < bestLatency){
//                    bestLatency = latency;
//                    newHostId = participantID;
//                }
//            }
//        }
//
//        //3. 새로운 호스트 broadcast
//    }
}