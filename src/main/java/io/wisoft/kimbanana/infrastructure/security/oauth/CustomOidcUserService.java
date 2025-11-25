package io.wisoft.kimbanana.infrastructure.security.oauth;

import static java.util.Collections.singleton;

import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name  = oidcUser.getAttribute("name");
        String sub   = oidcUser.getAttribute("sub"); // 구글 고유 ID

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(
                    new org.springframework.security.oauth2.core.OAuth2Error("invalid_userinfo"),
                    "프로필에 이메일이 없습니다. scope(openid, email, profile)를 확인하세요."
            );
        }

        // 3) upsert
        User user = userRepository.findByEmail(email)
                .map(u -> u.update(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .password(null)
                        .provider("google")
                        .providerId(sub)
                        .build());

        userRepository.save(user);

        Map<String, Object> enriched = new HashMap<>(oidcUser.getAttributes());
        enriched.put("user_id", user.getId());

        return new DefaultOidcUser(
                singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "sub"
        ) {
            @Override
            public Map<String, Object> getAttributes() {
                return enriched;
            }
        };
    }
}