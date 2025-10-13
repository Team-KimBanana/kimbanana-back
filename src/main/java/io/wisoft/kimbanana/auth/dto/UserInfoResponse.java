package io.wisoft.kimbanana.auth.dto;

public record UserInfoResponse(
        String id,
        String email,
        String name
) {
}
