package com.example.vrt.domain.room.service;

import com.example.vrt.domain.room.dto.RoomJoinRequestDTO;
import com.example.vrt.domain.room.dto.RoomJoinResponseDTO;

public interface RoomService {
    RoomJoinResponseDTO joinRoom(RoomJoinRequestDTO requestDTO);
}