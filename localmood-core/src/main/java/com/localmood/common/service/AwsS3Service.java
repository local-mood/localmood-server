package com.localmood.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile multipartFile, String fileName, String dirName) throws IOException {
    File uploadFile = convert(multipartFile)
      .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));

    fileName = dirName + fileName;
    String uploadImageUrl = putS3(uploadFile, fileName);
    removeNewFile(uploadFile);

    return uploadImageUrl;
  }

  private String putS3(File uploadFile, String fileName) {
    amazonS3Client.putObject(
      new PutObjectRequest(bucket, fileName, uploadFile)
        .withCannedAcl(CannedAccessControlList.PublicRead));

    return bucket + ".s3.amazonaws.com/" + fileName;
  }

  private void removeNewFile(File targetFile) {
    if(targetFile.delete()) {
      log.info("파일이 삭제되었습니다.");
    }else {
      log.info("파일이 삭제되지 못했습니다.");
    }
  }

  private Optional<File> convert(MultipartFile file) throws IOException {
    File convertFile = new File(file.getOriginalFilename());
    if(convertFile.createNewFile()) {
      try (FileOutputStream fos = new FileOutputStream(convertFile)) {
        fos.write(file.getBytes());
      }
      return Optional.of(convertFile);
    }
    return Optional.empty();
  }

}