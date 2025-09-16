package io.wisoft.kimbanana.image.handler;

import io.wisoft.kimbanana.image.service.CleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCleanupScheduler {

    private final CleanupService cleanupService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void cleanExpiredImages() {
        log.info("이미지 정리 작업 - 업로드 기준 1개월 초과 이미지 삭제");

        try {
            cleanupService.cleanExpiredImages();
        } catch (Exception e) {
            log.error("이미지 삭제중 오류: {}", e.getMessage());
        }
    }
}