package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.*;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomJoinResponseDTO joinRoom(RoomJoinRequestDTO requestDTO) {
        // 1. roomId로 방 조회
        Room room = roomRepository.findById(requestDTO.getId())
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
                room.getGallery().getGalleryId(),
                room.getHostUserEndpoint(),
                isHost,
                room.getMapFileURL(),
                room.getCurrentParticipants(),
                room.getMaxParticipants()
        );
    }
}