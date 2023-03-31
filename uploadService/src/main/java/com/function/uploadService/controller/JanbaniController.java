package com.function.uploadService.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.function.uploadService.domain.janban.FeatureRequest;
import com.function.uploadService.domain.janban.Janbani;
import com.function.uploadService.service.ImageService;
import com.function.uploadService.service.JanbaniService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JanbaniController {

	private final JanbaniService janbaniService;
	private final ImageService imageService;

	@PostMapping("/janbani")
	public ResponseEntity<Janbani> createJanbani(
		@RequestBody FeatureRequest request) {
		Janbani janbani = janbaniService.createJanban(request.getUserId(), request.getFeature());
		return ResponseEntity.status(HttpStatus.CREATED).body(janbani);
	}

	@GetMapping("/janbani")
	public ResponseEntity<?> getImage(@RequestParam String name) throws IOException {
		Resource resource = imageService.getImage(name);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed: " + e.getMessage());
	}

}

