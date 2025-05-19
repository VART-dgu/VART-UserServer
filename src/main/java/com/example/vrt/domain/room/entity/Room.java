package com.example.vrt.domain.room.entity;

import com.example.vrt.domain.gallery.domain.Gallery;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    private String hostUserId;
    private String hostUserEndpoint;

    private int maxParticipants = 30;
    private int currentParticipants = 0;
    private String mapFileURL;

    @ElementCollection
    private List<String> participantIDs = new ArrayList<>();
}