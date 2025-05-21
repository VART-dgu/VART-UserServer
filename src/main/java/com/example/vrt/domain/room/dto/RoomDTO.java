package com.example.vrt.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private String galleryId;
    private String hostUserId;
    private String hostUserEndpoint;
    private int maxParticipants;
    private int currentParticipants;
    private List<String> participantIDs;
    private String mapFileURL;
}
