package io.wisoft.kimbanana.invitation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record InvitationResponse(
        String token,
        
        @JsonProperty("presentation_id")
        String presentationId,
        
        @JsonProperty("expires_at")
        LocalDateTime expiresAt,
        
        @JsonProperty("invitation_url")
        String invitationUrl
) {}