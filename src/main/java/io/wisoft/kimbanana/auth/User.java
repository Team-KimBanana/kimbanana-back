package io.wisoft.kimbanana.auth;

public record User(
        String id,
        String email,
        String name,
        String password,
        String provider,
        String providerId
) {
}
