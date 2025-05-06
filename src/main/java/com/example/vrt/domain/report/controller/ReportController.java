package com.example.vrt.domain.report.controller;

import com.example.vrt.domain.report.dto.ReportRequestDTO;
import com.example.vrt.domain.report.service.ReportService;
import com.example.vrt.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReport(@RequestBody ReportRequestDTO dto) {
        reportService.createReport(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "COMMON200", "성공입니다.", "")
        );
    }
}