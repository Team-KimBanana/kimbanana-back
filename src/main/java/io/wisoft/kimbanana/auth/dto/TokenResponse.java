package io.wisoft.kimbanana.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
