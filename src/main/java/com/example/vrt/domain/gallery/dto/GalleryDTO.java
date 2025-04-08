package com.example.vrt.domain.gallery.dto;

import com.example.vrt.domain.gallery.domain.Gallery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class GalleryDTO {
    private String id;
    private String artist;
    private String description;
    private Integer visitCount;
}