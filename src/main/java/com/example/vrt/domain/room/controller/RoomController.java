package com.example.vrt.domain.room.controller;

import com.example.vrt.domain.room.dto.RoomJoinRequestDTO;
import com.example.vrt.domain.room.dto.RoomJoinResponseDTO;
import com.example.vrt.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/join")
    public ResponseEntity<RoomJoinResponseDTO> joinRoom(@RequestBody RoomJoinRequestDTO requestDTO) {
        RoomJoinResponseDTO responseDTO = roomService.joinRoom(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}