package io.wisoft.kimbanana.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
