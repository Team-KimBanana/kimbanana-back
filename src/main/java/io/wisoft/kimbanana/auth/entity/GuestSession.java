package io.wisoft.kimbanana.auth.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuestSession {
    private String guestId;
    private String presentationId;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
