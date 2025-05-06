package com.example.vrt.domain.gallery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "Gallery")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long key;

    private String id;
    private String artist;
    private String description;
    private Integer visitCount;
}