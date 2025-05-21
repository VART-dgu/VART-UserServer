package com.example.vrt.domain.gallery.service;

import com.example.vrt.domain.gallery.dto.GalleryDTO;
import com.example.vrt.domain.gallery.dto.GalleryInfoDTO;
import com.example.vrt.domain.gallery.dto.RoomDTO;
import com.example.vrt.domain.gallery.mapper.GalleryInfoMapper;
import com.example.vrt.domain.gallery.mapper.GalleryMapper;
import com.example.vrt.domain.gallery.mapper.RoomMapper;
import com.example.vrt.domain.gallery.repository.GalleryRepository;
import com.example.vrt.domain.room.repository.RoomRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GalleryInfoService {
    private final GalleryRepository galleryRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public GalleryInfoDTO getGalleryInfo(String galleryId) {
        GalleryDTO galleryDTO = GalleryMapper.toGalleryDTO(galleryRepository.findById(galleryId).orElseThrow(()-> new IllegalArgumentException("해당 갤러리가 존재하지 않습니다. ID: " + galleryId)));
        List<RoomDTO> roomDTOs = roomRepository.findAllByGallery_Id(galleryId).stream()
                .map(room -> RoomMapper.toRoomDTO(room))
                .collect(Collectors.toList());

        return GalleryInfoMapper.toGalleryInfoDTO(galleryDTO, roomDTOs);
    }
}