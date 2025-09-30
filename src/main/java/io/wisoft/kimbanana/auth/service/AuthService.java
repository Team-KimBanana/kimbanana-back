package io.wisoft.kimbanana.auth.service;

import io.wisoft.kimbanana.auth.User;
import io.wisoft.kimbanana.auth.dto.SignInRequest;
import io.wisoft.kimbanana.auth.dto.SignUpRequest;
import io.wisoft.kimbanana.auth.dto.TokenResponse;
import io.wisoft.kimbanana.auth.jwt.JwtTokenProvider;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;


    public Integer signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(encoder.encode(request.getPassword()))
                .provider("kimbanana")
                .providerId("jwt_kimbanana")
                .build();

        return userRepository.save(user);
    }


    public TokenResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh 토큰이 유효하지 않음");
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(email);

        return new TokenResponse(newAccessToken, refreshToken); // 기존 refresh 재사용
    }
}
