package yjj.wetrash.domain.member.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ProfileUploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket")
    private String bucketName;

    public String uploadFile(MultipartFile file){
       try {
           String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

           PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                   .bucket(bucketName)
                   .key(fileName)
                   .contentType(file.getContentType())
                   .contentLength(file.getSize())
                   .build();
           s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return "https://" + bucketName + ".s3."
                    + s3Client.serviceClientConfiguration().region().id() + ".amazonaws.com/" + fileName;
       } catch (IOException e){
           throw new RuntimeException("파일 업로드 실패", e);
       }

    }

}
