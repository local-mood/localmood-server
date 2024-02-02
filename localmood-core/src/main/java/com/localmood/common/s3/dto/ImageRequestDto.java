package com.localmood.common.s3.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRequestDto {

	@Schema(description = "이미지", nullable = true)
	private List<MultipartFile> multipartFiles;

}
