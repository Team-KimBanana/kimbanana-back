package io.wisoft.kimbanana.image.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageService {
    private static final String UPLOAD_DIR = "uploads/slide-images";
    private static final String URL_PATH_PREFIX = "/slide-images/";


    public boolean deleteFile(String imageUrl) {
        String fileName = imageUrl.replace(URL_PATH_PREFIX, "");
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("파일 삭제 성공: {}", filePath);
            } else {
                log.warn("파일이 존재 하지 않음: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            return false;
        }
    }


    public void clearDirectory(final Path dir) {
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 정리 실패: " + e.getMessage(), e);
            }
        }
    }
}
