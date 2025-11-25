package io.wisoft.kimbanana.invitation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateInvitationResponse(
        boolean valid,

        @JsonProperty("guest_token")
        String guestToken,

        @JsonProperty("presentation_id")
        String presentationId,

        String message
) {}