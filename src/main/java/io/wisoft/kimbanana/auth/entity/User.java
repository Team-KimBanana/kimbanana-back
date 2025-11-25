package io.wisoft.kimbanana.auth.entity;

import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
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

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}