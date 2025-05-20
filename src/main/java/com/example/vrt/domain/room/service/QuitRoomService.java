package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomDTO;
import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.entity.Room;
import com.example.vrt.domain.room.mapper.RoomMapper;
import com.example.vrt.domain.room.repository.RoomRepository;
import com.example.vrt.global.ping.PingResult;
import com.example.vrt.global.ping.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuitRoomService {
    private final RoomRepository roomRepository;
    private final PingService pingService;

    public RoomQuitResponseDTO quitRoom(RoomQuitRequestDTO requestDTO) {
        //1. 사용자 제거

        //방 조회
        Room room = roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room을 찾을 수 없음"));

        //방 사용자에서 사용자 제거
        List<String> participantIDs = room.getParticipantIDs();

        for(int i = 0; i < participantIDs.size(); i++){
            if(participantIDs.get(i).equals(requestDTO.getUserId())){
                room.getParticipantIDs().remove(i);
                break;
            }
        }

        //2. 호스트라면 모든 사용자에게 핑 테스트, 가장 낮은 핑을 새로운 호스트로 선정, 저장
        if(requestDTO.getIsHost()){
            long[] latencies = new long[room.getParticipantIDs().size()];


            //각 participant에 ping 채우기
            for (int i = 0; i < participantIDs.size(); i++) {
                PingResult pingResult = pingService.ping(participantIDs.get(i));

                if (pingResult.isSuccess()) {
                    latencies[i] = pingResult.getLatency();
                } else {
                    latencies[i] = Long.MAX_VALUE;  // 실패 시 최하값이 되지 않도록
                }
            }

            //최솟값 index 찾기
            int minIdx = 0;
            for (int i = 1; i < latencies.length; i++) {
                if (latencies[i] < latencies[minIdx]) {
                    minIdx = i;
                }
            }

            String newHostId = participantIDs.get(minIdx);
            room.setHostUserId(newHostId);
        }

        //3. 새로운 호스트 broadcast
    }
}
