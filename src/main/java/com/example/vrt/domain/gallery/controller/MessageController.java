package com.example.vrt.domain.gallery.controller;

import com.example.vrt.domain.gallery.dto.GalleryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class MessageController {
    @MessageMapping("/chat") // /app/chat 목적지로 들어오는 메시지 처리
    @SendTo("/topic/gallery") //broadcast 경로
    public GalleryDTO sendGallery(@Payload GalleryDTO galleryDTO) {
        log.info(galleryDTO.toString());
        return galleryDTO;
    }
}
