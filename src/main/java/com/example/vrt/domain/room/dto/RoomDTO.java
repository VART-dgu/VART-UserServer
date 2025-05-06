package com.example.vrt.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
@Builder
public class RoomDTO {
    private String id;
    private String galleryId;
    private String hostUserId;
    private int maxParticipants;
    private int currentParticipants;
    private List<String> participantIDs;
}
