package com.example.vrt.domain.gallery.service;

import com.example.vrt.domain.gallery.dto.GalleryDTO;
import com.example.vrt.domain.gallery.mapper.GalleryMapper;
import com.example.vrt.domain.gallery.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GalleryService {
    private final GalleryRepository galleryRepository;

    public List<GalleryDTO> getAllGallery(){
        return galleryRepository.findAll().stream()
                .map(gallery -> GalleryMapper.toGalleryDTO(gallery))
                .collect(Collectors.toList());
    }
}
