package com.example.vrt.domain.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomJoinRequestDTO {
    private Long id;
    private Long galleryId;
    private String userId;
    private String userEndpoint;
}