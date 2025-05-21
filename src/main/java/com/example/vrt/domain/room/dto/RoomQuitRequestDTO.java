package com.example.vrt.domain.room.dto;

import lombok.Getter;

@Getter
public class RoomQuitRequestDTO {
    Long id;
    String galleryId;
    String userId;
    Boolean isHost;
    String sessionId;
}
