package com.localmood.common.s3.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.localmood.common.s3.service.AwsS3Service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "File", description = "파일 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class AmazonS3Controller {

	private final AwsS3Service awsS3Service;

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(
		@RequestPart(value = "file", required = true) MultipartFile multipartFile) {

		String fileUrl = awsS3Service.uploadFile(multipartFile);

		return ResponseEntity.ok(fileUrl);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile/temp",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFileTemp(
		@RequestParam(value = "file", required = true) MultipartFile multipartFile) {

		String fileUrl = awsS3Service.uploadFile(multipartFile);

		return ResponseEntity.ok(fileUrl);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile/temp/2")
	public ResponseEntity<String> uploadFileTemp2(
		@RequestPart(value = "file", required = true) MultipartFile multipartFile) {

		String fileUrl = awsS3Service.uploadFile(multipartFile);

		return ResponseEntity.ok(fileUrl);
	}


}