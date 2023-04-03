package com.hellsfood.api;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hellsfood.service.ImageService;
import com.hellsfood.service.JanbaniService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;
	private final JanbaniService janbaniService;

	@ApiOperation(value = "S3에 이미지 업로드")
	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(
		@RequestParam("file") MultipartFile file,
		@RequestParam("propsName") String propsName) throws IOException {

		imageService.uploadImage(file, propsName);
		return ResponseEntity.ok("Image upload successful!");
	}

	@ApiOperation(value = "{id}에 해당하는 잔반이 조회")
	@GetMapping("/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id) {
		Resource resource = imageService.getImage(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "{name}에 해당하는 잔반이 조회")
	@GetMapping("/name")
	public ResponseEntity<?> getImage(@RequestParam String name) {
		Resource resource = imageService.getImage(name);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "유저별 잔반이 조회")
	@GetMapping("")
	public ResponseEntity<?> getJanbani(@RequestParam String userId) {
		String janbanCode = janbaniService.getJanban(userId);
		Resource resource = imageService.getImage(janbanCode);
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
