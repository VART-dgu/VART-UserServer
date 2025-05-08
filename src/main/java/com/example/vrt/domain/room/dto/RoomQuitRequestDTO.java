package com.example.vrt.domain.room.dto;

import lombok.Getter;

@Getter
public class RoomQuitRequestDTO {
    String roomId;
    String galleryId;
    String userId;
    Boolean isHost;
}
