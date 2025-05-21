package com.example.vrt.domain.gallery.repository;

import com.example.vrt.domain.gallery.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
