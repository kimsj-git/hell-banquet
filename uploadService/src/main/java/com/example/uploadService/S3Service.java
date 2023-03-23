package com.example.uploadService;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	@Value("${aws.s3.accessKey}")
	private String accessKey;

	@Value("${aws.s3.secretKey}")
	private String secretKey;

	@Value("${aws.s3.region}")
	private String region;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	public String upload(MultipartFile file) throws IOException {

		String fileName = file.getOriginalFilename();
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));
		String newFileName = UUID.randomUUID().toString() + fileExtension;

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			.withRegion(region)
			.build();

		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFileName, file.getInputStream(), metadata);
			s3Client.putObject(putObjectRequest);
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		}

		return s3Client.getUrl(bucketName, newFileName).toString();
	}
}
