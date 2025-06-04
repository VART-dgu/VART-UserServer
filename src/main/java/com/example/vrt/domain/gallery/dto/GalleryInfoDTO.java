package com.example.vrt.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
@Builder
public class GalleryInfoDTO {
    private Long id;
    private String artist;
    private String description;
    private List<RoomDTO> roomList;
}
