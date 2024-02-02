package com.localmood.common.s3.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.localmood.common.exception.ErrorCode;
import com.localmood.common.exception.LocalmoodException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	public String uploadFile(MultipartFile file) {
		try {
			String fileName = generateFileName(file);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType());
			amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

			// 업로드된 파일 URL 생성
			String fileUrl = generateFileUrl(fileName);
			return fileUrl;
		} catch (IOException e) {
			throw new LocalmoodException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}

	// 파일명 생성
	private String generateFileName(MultipartFile file) {
		return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
	}

	// 파일 타입과 상관없이 업로드할 수 있게 하도록 "." 존재 유무만 판단
	private String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
		}
	}

	// 파일 url 생성
	private String generateFileUrl(String fileName) {
		return bucket + ".s3.amazonaws.com/" + fileName;
	}
}

