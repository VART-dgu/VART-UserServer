package com.example.vrt.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@AllArgsConstructor
public class RoomQuitResponseDTO {
    Long roomId;
    String galleryId;
    String hostUserEndpoint;
    Boolean isHost;
    String mapFileURL;
}
