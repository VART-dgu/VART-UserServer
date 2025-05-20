package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomDTO;
import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.mapper.RoomMapper;
import com.example.vrt.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuitRoomService {
    private final RoomRepository roomRepository;

    public RoomQuitResponseDTO quitRoom(RoomQuitRequestDTO requestDTO) {
        //1. 사용자 제거

        //방 조회
        RoomDTO roomDTO = RoomMapper.toRoomDTO(roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room을 찾을 수 없음")));

        List<String> participantIDs = roomDTO.getParticipantIDs();

        //방 사용자에서 사용자 제거
        for(String p: participantIDs){
            if(p.equals(requestDTO.getUserId()){
                participantIDs.remove(p);
                break;
            }
        }

        //2. 호스트라면 모든 사용자에게 핑 테스트, 가장 낮은 핑을 새로운 호스트로 선정, 저장
        if(requestDTO.getIsHost()){
            String newHostId = null;
            long bestLatency = Long.MAX_VALUE;
            for(String s : roomDTO.getParticipantIDs()){
                long latency = pingService.ping(s);
                if(latency < bestLatency){
                    bestLatency = latency;
                    newHostId = participantID;
                }
            }
        }

        //3. 새로운 호스트 broadcast
    }
}
