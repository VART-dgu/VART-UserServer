package com.example.vrt.domain.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDTO {
    private String reporterId;
    private String respondentId;
    private String description;
}