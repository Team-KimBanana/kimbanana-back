package io.wisoft.kimbanana.image.repository;

import io.wisoft.kimbanana.image.entity.SlideImage;
import java.time.LocalDateTime;
import java.util.List;

public interface ImageUploadRepository {
    void saveSlideImage(SlideImage slideImage);
    void savePresentationThumbnail(final String responseUrl, final String presentationId);
    List<SlideImage> findExpiredImages(LocalDateTime now);
    int deleteAllBySlideIds(List<String> slideIds);
}
