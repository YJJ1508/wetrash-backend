package yjj.wetrash.domain.member.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ProfileImgUploader {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-url}")
    private String accessUrl;

    public String saveFile(MultipartFile file){
        try{
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.createDirectories(filePath.getParent()); //디렉터리 없으면 생성
            Files.write(filePath, file.getBytes());
            return "http://localhost:8080" + accessUrl + filename; //클라이언트에서 접근할 url (임시로 local)
        } catch (IOException e){
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
