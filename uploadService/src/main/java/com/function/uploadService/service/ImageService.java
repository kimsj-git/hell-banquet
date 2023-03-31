package com.function.uploadService.service;

import static com.function.uploadService.domain.image.JanbanCode.*;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.function.uploadService.domain.image.Image;
import com.function.uploadService.domain.image.ImageRepository;
import com.function.uploadService.domain.image.JanbanCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final S3Service s3Service;

	public void uploadImage(MultipartFile file, String janbanName) throws IOException {
		String filePath = s3Service.uploadFileToS3(file);

		Image image = Image.builder()
			.filePath(filePath)
			.janbanCode(fromValue(janbanName))
			.build();
		imageRepository.save(image);
	}

	public Resource getImage(Long id) {
		Image image = imageRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 잔반이가 없습니다."));

		return s3Service.readImageFromS3(image.getFilePath());
	}

	public Resource getImage(String code) {
		// JanbanCode code = fromValue(name);
		JanbanCode janbanCode = fromCode(code);
		Image image = imageRepository.findOneByJanbanCode(janbanCode);
		return s3Service.readImageFromS3(image.getFilePath());
	}

}
