package io.wisoft.kimbanana.image.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SlideImage {
    private String id;
    private String imageUrl;
    private LocalDateTime uploadedAt;
    private LocalDateTime expiresAt;
    private boolean deleted;
}
