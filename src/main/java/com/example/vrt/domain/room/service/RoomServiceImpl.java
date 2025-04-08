package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomJoinRequestDTO;
import com.example.vrt.domain.room.dto.RoomJoinResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    @Override
    public RoomJoinResponseDTO joinRoom(RoomJoinRequestDTO requestDTO) {
        // 예시 구현 - 실제 로직은 이후 구현 필요
        return new RoomJoinResponseDTO(
                1,
                requestDTO.getGalleryId(),
                "12:31:ab",     // 임시 host endpoint
                true,           // 임시 호스트 여부
                "google.com~"   // 임시 맵 파일 경로
        );
    }
}