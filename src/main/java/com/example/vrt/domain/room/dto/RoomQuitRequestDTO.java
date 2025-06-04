package com.example.vrt.domain.room.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomQuitRequestDTO {
    Long id;
    String galleryId;
    String userId;
    Boolean isHost;
    String sessionId;
}
