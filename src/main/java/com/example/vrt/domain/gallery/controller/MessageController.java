package com.example.vrt.domain.gallery.controller;

import com.example.vrt.domain.gallery.dto.GalleryDTO;
import com.example.vrt.domain.gallery.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final GalleryService galleryService;

    @MessageMapping("/gallery/list") // /app/chat 목적지로 들어오는 메시지 처리
    @SendTo("/topic/gallery/list") //broadcast 경로
    public List<GalleryDTO> sendGallery() {

        return galleryService.getAllGallery();
    }
}
