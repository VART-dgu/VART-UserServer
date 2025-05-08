package com.example.vrt.domain.room.controller;

import com.example.vrt.domain.room.dto.RoomJoinRequestDTO;
import com.example.vrt.domain.room.dto.RoomJoinResponseDTO;
import com.example.vrt.domain.room.dto.RoomQuitRequestDTO;
import com.example.vrt.domain.room.dto.RoomQuitResponseDTO;
import com.example.vrt.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

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

//    @PostMapping("/quit")
//    public ResponseEntity<RoomQuitResponseDTO> quitRoom(@RequestBody RoomQuitRequestDTO requestDTO) {
//        RoomQuitResponseDTO responseDTO;
//
//
//    }

}