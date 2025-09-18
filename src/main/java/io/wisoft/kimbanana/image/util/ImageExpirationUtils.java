package io.wisoft.kimbanana.image.util;

import java.time.LocalDateTime;

public class ImageExpirationUtils {

    public static final int RETENTION_DAYS = 30;

    /**
     * 만료일 계산
     */
    public static LocalDateTime calculateExpirationDate(LocalDateTime uploadedAt) {
        return uploadedAt != null ? uploadedAt.plusDays(RETENTION_DAYS) : null;
    }

}
