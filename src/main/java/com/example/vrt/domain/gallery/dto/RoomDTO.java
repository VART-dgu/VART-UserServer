package com.example.vrt.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private String hostUserId;
    private int maxParticipants;
    private int currentParticipants;
    private List<String> participantIDs;
}
