package io.wisoft.kimbanana.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String name;
    private String password;
    private String provider;
    private String providerId;

    public User update(String name) {
        this.name = name;
        return this;
    }
}