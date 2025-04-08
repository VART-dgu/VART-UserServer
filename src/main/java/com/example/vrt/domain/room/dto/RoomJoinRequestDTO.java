package com.example.vrt.domain.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomJoinRequestDTO {
    private String roomId;
    private String galleryId;
    private String userId;
    private String userEndpoint;
}