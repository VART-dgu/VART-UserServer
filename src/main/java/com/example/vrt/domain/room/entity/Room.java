package com.example.vrt.domain.room.entity;

import com.example.vrt.domain.gallery.domain.Gallery;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    private String hostUserId;
    private String hostUserEndpoint;

    private int maxParticipants = 30;
    private int currentParticipants = 0;

    @Column(length = 50000)
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