package com.example.vrt.domain.room.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    private String id; // roomId

    private String galleryId;

    private String hostUserId;
    private String hostUserEndpoint;

    private int maxParticipants = 30;
    private int currentParticipants = 0;
    private String mapFileURL;

    @ElementCollection
    private List<String> participantIDs = new ArrayList<>();

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