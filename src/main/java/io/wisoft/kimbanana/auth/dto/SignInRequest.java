package io.wisoft.kimbanana.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignInRequest {
    private String email;
    private String password;
}
