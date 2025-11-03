package io.wisoft.kimbanana.invitation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateInvitationRequest(
        @JsonProperty("presentation_id")
        String presentationId,

        @JsonProperty("expires_in_hours")
        Integer expiresInHours
) {}