package com.example.vrt.domain.gallery.mapper;

import com.example.vrt.domain.gallery.domain.Gallery;
import com.example.vrt.domain.gallery.dto.GalleryDTO;

public class GalleryMapper {
    public static GalleryDTO toGalleryDTO(Gallery gallery) {
        return GalleryDTO.builder()
                .id(gallery.getId())
                .artist(gallery.getArtist())
                .description(gallery.getDescription())
                .visitCount(gallery.getVisitCount())
                .build();
    }
}
