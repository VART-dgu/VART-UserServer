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

    // --- 비즈니스 메서드 ---

    public void addParticipant(String userId) {
        if (!participantIDs.contains(userId)) {
            participantIDs.add(userId);
            currentParticipants++;
        }
    }

    public void removeParticipant(String userId) {
        if (participantIDs.remove(userId)) {
            currentParticipants--;
        }
    }

    public boolean isFull() {
        return currentParticipants >= maxParticipants;
    }

    public boolean isHost(String userId) {
        return userId.equals(this.hostUserId);
    }
}
