package com.example.uploadService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// @RequiredArgsConstructor
@RestController
public class UploadController {

	@Autowired
	private S3Service s3Service;

	@PostMapping("/upload")
	public String uploadFile(@RequestBody MultipartFile file) throws IOException {
		return s3Service.upload(file);
	}

}