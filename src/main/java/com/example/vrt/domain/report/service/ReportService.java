package com.example.vrt.domain.report.service;

import com.example.vrt.domain.report.dto.ReportRequestDTO;
import com.example.vrt.domain.report.entity.Report;
import com.example.vrt.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public void createReport(ReportRequestDTO dto) {
        Report report = new Report();
        report.setReporterId(dto.getReporterId());
        report.setRespondentId(dto.getRespondentId());
        report.setDescription(dto.getDescription());
        reportRepository.save(report);
    }
}

