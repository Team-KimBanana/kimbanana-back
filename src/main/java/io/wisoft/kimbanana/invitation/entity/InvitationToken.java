package io.wisoft.kimbanana.invitation.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvitationToken {
    private String token;
    private String presentationId;
    private String createdByUserId;
    private LocalDateTime expiresAt;

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }
}
