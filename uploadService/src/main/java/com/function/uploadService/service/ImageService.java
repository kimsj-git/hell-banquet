package com.function.uploadService.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.function.uploadService.domain.Image;
import com.function.uploadService.domain.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final S3Service s3Service;

	public void uploadImage(MultipartFile file, String propsName) throws IOException {
		String filePath = s3Service.uploadFileToS3(file);

		Image image = Image.builder()
			.filePath(filePath)
			.propsName(propsName)
			.build();
		imageRepository.save(image);
	}

	public Resource getImage(Long id) {
		Image image = imageRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));

		return s3Service.readImageFromS3(image.getFilePath());
	}

}
