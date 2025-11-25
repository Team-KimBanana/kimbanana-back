package io.wisoft.kimbanana.auth.dto.request;

public record SignUpRequest(
        String name,
        String email,
        String password
) {
}
