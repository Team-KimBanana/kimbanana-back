package io.wisoft.kimbanana.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class User {
    private Long id;
    private String email;
    private String name;
    private String password;
}
