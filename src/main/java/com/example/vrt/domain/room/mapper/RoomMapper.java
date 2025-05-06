package com.example.vrt.domain.room.mapper;

import com.example.vrt.domain.gallery.dto.RoomDTO;
import com.example.vrt.domain.room.entity.Room;

public class RoomMapper {
    public static RoomDTO toRoomDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .hostUserId(room.getHostUserId())
                .maxParticipants(room.getMaxParticipants())
                .currentParticipants(room.getCurrentParticipants())
                .participantIDs(room.getParticipantIDs())
                .build();
    }
}
