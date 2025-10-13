package io.wisoft.kimbanana.auth.dto;

public record SignInRequest(
        String email,
        String password
) {
}
