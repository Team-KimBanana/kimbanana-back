package io.wisoft.kimbanana.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
}
