package com.localmood.common.s3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.localmood.common.s3.service.AwsS3Service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "File", description = "파일 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class AmazonS3Controller {

	private final AwsS3Service awsS3Service;

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
	public ResponseEntity<List<String>> uploadFile(
		@RequestParam(value = "file", required = true) MultipartFile[] multipartFiles) {
		List<String> uploadedFileUrls = new ArrayList<>();

		for (MultipartFile file : multipartFiles) {
			String fileUrl = awsS3Service.uploadFile(file);
			uploadedFileUrls.add(fileUrl);
		}

		return ResponseEntity.ok(uploadedFileUrls);
	}


}