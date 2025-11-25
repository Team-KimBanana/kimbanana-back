package io.wisoft.kimbanana.infrastructure.security.oauth;

import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println(">>> [OAUTH] loadUser() called");
        System.out.println(">>> [OAUTH] userRequest = " + userRequest);

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        System.out.println(">>> [OAUTH] attributes = " + oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        System.out.println(">>> [OAUTH] mapped attributes email=" + attributes.getEmail() + " name=" + attributes.getName());

        if (attributes.getEmail() == null || attributes.getEmail().isBlank()) {
            System.out.println(">>> [OAUTH] email missing -> throwing");
            throw new OAuth2AuthenticationException(new org.springframework.security.oauth2.core.OAuth2Error("invalid_userinfo"),
                    "프로필에 이메일이 없습니다. scope를 확인하세요.");
        }

        // upsert
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(u -> u.update(attributes.getName()))
                .orElse(attributes.toEntity());
        userRepository.save(user);
        System.out.println(">>> [OAUTH] user saved (email=" + user.getEmail() + ", provider=" + user.getProvider() + ")");


        java.util.Map<String, Object> enrichedAttributes = new java.util.HashMap<>(attributes.getAttributes());
        enrichedAttributes.put("user_id", user.getId());
        System.out.println(">>> [OAUTH] enrichedAttributes.user_id=" + user.getId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}