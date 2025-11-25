package io.wisoft.kimbanana.image.service;

import io.wisoft.kimbanana.image.entity.SlideImage;
import io.wisoft.kimbanana.image.repository.jdbc.JdbcImageUploadRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CleanupService {

    private final JdbcImageUploadRepository imageUploadRepository;
    private final StorageService storageService;

    @Transactional
    public void cleanExpiredImages() {
        try {
            List<SlideImage> expiredImages = getExpiredImages();

            if (expiredImages.isEmpty()) {
                log.info("삭제할 만료된 이미지가 없습니다.");
                return;
            }
            int deletedFileCount = deletePhysicalFiles(expiredImages);
            int deletedDbCount = deleteFromDatabase(expiredImages);

            log.info("이미지 정리 작업 완료 - 파일삭제: {}건, DB삭제: {}건",
                    deletedFileCount, deletedDbCount);

        } catch (Exception e) {
            log.error("이미지 정리 작업 중 오류 발생", e);
        }
    }

    private List<SlideImage> getExpiredImages() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<SlideImage> expiresImages = imageUploadRepository.findExpiredImages(now);
            log.debug("만료 기준 시간: {}, 만료된 이미지 {}", now, expiresImages);
            return expiresImages;
        } catch (Exception e) {
            log.error("만료된 이미지 조회 중 오류 발생", e);
            throw e;
        }
    }

    private int deletePhysicalFiles(final List<SlideImage> images) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        images.parallelStream().forEach(image -> {
            try {
                if(storageService.deleteFile(image.getImageUrl())) {
                    successCount.incrementAndGet();
                    log.debug("파일 삭제 성공: {}" , image.getImageUrl());
                } else {
                    failedCount.incrementAndGet();
                    log.warn("파일 삭제 실패 {}", image.getImageUrl() );
                }
            } catch (Exception e) {
                failedCount.incrementAndGet();
                log.error("파일 삭제 중 예외 발생: {}", image.getImageUrl(), e);
            }
        });

        log.info("파일 삭제 완료 - 성공: {}건, 실패: {}건", successCount.get(), failedCount.get());
        return successCount.get();
    }

    private int deleteFromDatabase(final List<SlideImage> images) {
        try {
            List<String> slideIds = images.stream()
                    .map(SlideImage::getId)
                    .toList();

            int count = imageUploadRepository.deleteAllBySlideIds(slideIds);
            log.info("데이터베이스에서 {}건 삭제 완료", count);
            return count;
        } catch (Exception e) {
            log.error("데이터베이스 삭제 중 오류 발생", e);
            throw e;
        }
    }


}
