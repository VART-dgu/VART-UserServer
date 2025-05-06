package com.example.vrt.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterId;
    private String respondentId;
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
}