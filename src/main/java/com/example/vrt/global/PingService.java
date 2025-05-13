package com.example.vrt.global;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PingService {
//    private final RestTemplate restTemplate;
//
//    @Scheduled(fixedRateString = "${ping.fixed-rate:30000}")
//    public Long ping(String url) {
//        long start = System.nanoTime();
//        try {
//            // HEAD 요청만 보내서 바디는 받지 않음
//            restTemplate.headForHeaders(url);
//            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
//        } catch (Exception e) {
//            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
//        }
//    }
}
