package com.example.vrt.domain.gallery.controller;

import com.example.vrt.domain.gallery.dto.GalleryDTO;
import com.example.vrt.domain.gallery.dto.GalleryInfoDTO;
import com.example.vrt.domain.gallery.dto.GalleryInfoRequestDTO;
import com.example.vrt.domain.gallery.service.GalleryInfoService;
import com.example.vrt.domain.gallery.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final GalleryService galleryService;
    private final GalleryInfoService galleryInfoService;

    @MessageMapping("/gallery/list") // /app/chat 목적지로 들어오는 메시지 처리
    @SendTo("/topic/gallery/list") //broadcast 경로
    public List<GalleryDTO> sendGallery() {

        return galleryService.getAllGallery();
    }

    @MessageMapping("/gallery/info")
    @SendTo("/topic/gallery/info")
    public GalleryInfoDTO sendGalleryInfo(@Payload GalleryInfoRequestDTO galleryInfoRequestDTO) {
        return galleryInfoService.getGalleryInfo(galleryInfoRequestDTO.getGalleryId());
    }
}