package com.example.vrt.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomJoinResponseDTO {
    private int roomId;
    private String galleryId;
    private String hostUserEndpoint;
    private boolean isHost;
    private String mapFileURL;
    private int currentParticipants;
    private int maxParticipants;
}