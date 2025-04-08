package com.example.vrt.domain.gallery.dto;

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

/*
{
    "id" : "1",
    "artist" : "peter",
    "description" : "hi",
    "visitCount" : 1
}
 */
