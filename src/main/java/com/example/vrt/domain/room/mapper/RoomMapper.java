package com.example.vrt.domain.room.mapper;

import com.example.vrt.domain.room.dto.RoomDTO;
import com.example.vrt.domain.room.entity.Room;

public class RoomMapper {
    public static RoomDTO toRoomDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .hostUserId(room.getHostUserId())
                .maxParticipants(room.getMaxParticipants())
                .currentParticipants(room.getCurrentParticipants())
                .participantIDs(room.getParticipantIDs())
                .hostUserEndpoint(room.getHostUserEndpoint())
                .mapFileURL(room.getMapFileURL())
                .build();
    }
}

    public static Room toRoom(RoomDTO roomDTO) {
        return Room.builder()
                .id(roomDTO.getId())
                .hostUserId(roomDTO.getHostUserId())
                .hostUserEndpoint(roomDTO.getHostUserEndpoint())
                .maxParticipants(roomDTO.getMaxParticipants())
                .currentParticipants(roomDTO.getCurrentParticipants())
                .participantIDs(roomDTO.getParticipantIDs())
                .hostUserEndpoint(roomDTO.getHostUserEndpoint())
                .mapFileURL(roomDTO.getMapFileURL())
                .build();
    }
}