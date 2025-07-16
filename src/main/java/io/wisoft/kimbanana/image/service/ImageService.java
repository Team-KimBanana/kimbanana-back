
package io.wisoft.kimbanana.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private static final String UPLOAD_DIR = "uploads/slide-images";
    private static final String URL_PATH_PREFIX = "/slide-images/";

    public String uploadImage(MultipartFile image) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = UUID.randomUUID().toString().substring(0, 10) + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            image.transferTo(filePath);

            return URL_PATH_PREFIX + fileName;

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패" + e);
        }

    }
}
