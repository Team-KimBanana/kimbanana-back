package io.wisoft.kimbanana.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActiveUser(
        String id,
        String name,
        
        @JsonProperty("user_type")
        UserType userType,
        
        @JsonProperty("session_id")
        String sessionId
) {
    public enum UserType {
        USER, GUEST
    }
}