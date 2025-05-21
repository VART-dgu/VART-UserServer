package com.example.vrt.global.ping;

import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PingService {
    public PingResult ping(String urlString) {
        long start = System.currentTimeMillis();
        try {
            // URL 스킴이 없는 경우 http 붙여주기
            if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                urlString = "http://" + urlString;
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");           // GET 대신 HEAD 사용해서 페이로드 최소화
            conn.setConnectTimeout(3000);            // 연결 타임아웃 3초
            conn.setReadTimeout(3000);               // 읽기 타임아웃 3초
            conn.connect();

            int code = conn.getResponseCode();
            long latency = System.currentTimeMillis() - start;

            boolean isSuccess = (200 <= code && code <= 399);
            return new PingResult(isSuccess, latency);
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            return new PingResult(false, latency);
        }
    }
}