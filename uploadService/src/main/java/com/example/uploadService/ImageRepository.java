package com.example.uploadService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findById(Long id);
}
