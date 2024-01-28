package com.localmood.common.s3.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.localmood.common.s3.service.AwsS3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class AmazonS3Controller {

	private final AwsS3Service awsS3Service;

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<String>> uploadFile(
		@RequestPart(value = "file", required = true) List<MultipartFile> multipartFiles) {
		return ResponseEntity.ok(awsS3Service.uploadFile(multipartFiles));
	}

}