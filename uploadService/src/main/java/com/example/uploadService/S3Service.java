package com.example.uploadService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 s3Client;

	@Value("${cloud.aws.credentials.accessKey}")
	private String bucketName;

	public Resource readImageFromS3(String filePath) {
		try {
			// S3에서 객체(파일)를 가져옴
			S3Object s3Object = s3Client.getObject(bucketName, filePath);

			// S3에서 가져온 객체(파일)를 ByteArrayResource로 변환
			ByteArrayResource resource = new ByteArrayResource(s3Object.getObjectContent().readAllBytes());

			// S3에서 가져온 객체(파일) 스트림 닫기
			s3Object.close();

			return resource;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
