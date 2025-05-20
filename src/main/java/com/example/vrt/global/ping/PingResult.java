package com.example.vrt.global.ping;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PingResult {
    private final boolean isSuccess;
    private final long latency;   // ms 단위
}
