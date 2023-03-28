package com.example.uploadService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@RestController
@RequestMapping("/images")
public class ImageController {

	private final AmazonS3 amazonS3Client;
	private final ImageRepository imageRepository;
	private final S3Service s3Service;

	public ImageController(AmazonS3 amazonS3Client, ImageRepository imageRepository, S3Service s3Service) {
		this.amazonS3Client = amazonS3Client;
		this.imageRepository = imageRepository;
		this.s3Service = s3Service;
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(
		@RequestParam("file") MultipartFile file,
		@RequestParam("keyword") String keyword) {
		try {
			// S3에 파일 업로드
			String filePath = uploadFileToS3(file);

			// MySQL DB에 데이터 저장
			Image image = new Image();
			image.setFilePath(filePath);
			image.setKeyword(keyword);
			imageRepository.save(image);

			return ResponseEntity.ok("Image upload successful!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Image upload failed: " + e.getMessage());
		}
	}

	private String uploadFileToS3(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		String filePath = "images/" + UUID.randomUUID() + "_" + fileName;
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		String bucketName = "leftover-bucket";
		amazonS3Client.putObject(bucketName, filePath, file.getInputStream(), metadata);
		return filePath;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id) {
		Optional<Image> image = imageRepository.findById(id);

		if(image.isPresent()) {
			Resource resource = s3Service.readImageFromS3(image.get().getFilePath());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return new ResponseEntity<>(resource, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
