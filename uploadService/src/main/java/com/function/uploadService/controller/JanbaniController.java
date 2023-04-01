package com.function.uploadService.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.function.uploadService.domain.janban.FeatureRequest;
import com.function.uploadService.domain.janban.Janbani;
import com.function.uploadService.service.ImageService;
import com.function.uploadService.service.JanbaniService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/janban")
@RequiredArgsConstructor
public class JanbaniController {

	private final JanbaniService janbaniService;
	private final ImageService imageService;

	@ApiOperation(value = "특성에 따른 잔반이 만들기")
	@PutMapping()
	public ResponseEntity<Janbani> createJanbani(
		@RequestBody FeatureRequest request) {
		Janbani janbani = janbaniService.createJanban(request.getUserId(), request.getFeature());
		return ResponseEntity.status(HttpStatus.CREATED).body(janbani);
	}

	@ApiOperation(value = "고유 코드로 잔반이 이미지 조회")
	@GetMapping()
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

