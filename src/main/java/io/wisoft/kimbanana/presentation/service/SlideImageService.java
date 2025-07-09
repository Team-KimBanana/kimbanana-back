package io.wisoft.kimbanana.presentation.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class SlideImageService {

    private final String uploadDir = "/Users/boyeonshin/Developments/capstone/image/";

    public String uploadImage(MultipartFile image) {
        try{
            //디렉토리 생성
            Files.createDirectories(Paths.get(uploadDir));
            // 걉치지 않는 새로운 이름으호 파일 생성하기
            String filename = UUID.randomUUID() + "_" + originalFileName(image);
            Path filePath = Paths.get(uploadDir , filename);
            image.transferTo(filePath);

            return "/static/slide-image" + filename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패" , e);
        }
    }

    private String originalFileName(MultipartFile image) {
        return image.getOriginalFilename();
    }
}
