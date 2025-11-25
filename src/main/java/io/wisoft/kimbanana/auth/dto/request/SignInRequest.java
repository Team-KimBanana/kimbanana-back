package io.wisoft.kimbanana.auth.dto.request;

public record SignInRequest(
        String email,
        String password
) {
}
