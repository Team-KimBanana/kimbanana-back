package io.wisoft.kimbanana.auth.oauth;

import io.wisoft.kimbanana.auth.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // 먼저 기본 OIDC 유저 정보 받아오기 (id_token + userinfo)
        OidcUser oidcUser = super.loadUser(userRequest);

        // 구글 프로필에서 attribute 추출
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");
        String name  = (String) attributes.get("name");
        String sub   = (String) attributes.get("sub"); // Google unique id

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(
                    new org.springframework.security.oauth2.core.OAuth2Error("invalid_userinfo"),
                    "프로필에 이메일이 없습니다. scope(openid, email, profile)를 확인하세요."
            );
        }

        // upsert
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

        // attributes에 우리 user_id 추가
        Map<String, Object> enriched = new HashMap<>(attributes);
        enriched.put("user_id", user.getId());

        // DefaultOidcUser 다시 만들어서 반환
        return new org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "sub" // nameAttributeKey로 쓸 필드 (구글은 보통 "sub")
        ) {
            @Override
            public Map<String, Object> getAttributes() {
                return enriched;
            }
        };
    }
}
