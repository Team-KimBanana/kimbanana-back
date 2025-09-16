package io.wisoft.kimbanana.image.util;

import io.wisoft.kimbanana.image.entity.SlideImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ImageExpirationUtils {

    public static final int RETENTION_DAYS = 30;

    /**
     * 이미지 만료 여부
     */
    public static boolean isExpired(SlideImage slideImage) {
        if(slideImage == null || slideImage.getExpiresAt() == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(slideImage.getExpiresAt());
    }

    /**
     * 만료일 계산
     */
    public static LocalDateTime calculateExpirationDate(LocalDateTime uploadedAt) {
        return uploadedAt != null ? uploadedAt.plusDays(RETENTION_DAYS) : null;
    }


    /**
     * 만료된 이미지들만 필터링
     */
    public static List<SlideImage> filterExpiredImages(List<SlideImage> images) {
        return images.stream()
                .filter(ImageExpirationUtils::isExpired)
                .collect(Collectors.toList());
    }
}
