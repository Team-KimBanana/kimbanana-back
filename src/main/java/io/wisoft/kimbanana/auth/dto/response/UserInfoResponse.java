package io.wisoft.kimbanana.auth.dto.response;

public record UserInfoResponse(
        String id,
        String email,
        String name
) {
}
