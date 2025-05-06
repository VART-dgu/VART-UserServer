package com.example.vrt.domain.gallery.mapper;

import com.example.vrt.domain.gallery.domain.Gallery;
import com.example.vrt.domain.gallery.dto.GalleryDTO;
import com.example.vrt.domain.gallery.dto.GalleryInfoDTO;
import com.example.vrt.domain.gallery.dto.RoomDTO;

import java.util.List;

public class GalleryInfoMapper {
    public static GalleryInfoDTO toGalleryInfoDTO(GalleryDTO galleryDTO, List<RoomDTO> roomDTOS) {
        return GalleryInfoDTO.builder()
                .id(galleryDTO.getId())
                .artist(galleryDTO.getArtist())
                .description(galleryDTO.getDescription())
                .roomList(roomDTOS)
                .build();
    }
}
