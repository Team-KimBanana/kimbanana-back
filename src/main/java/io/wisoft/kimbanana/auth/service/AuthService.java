package io.wisoft.kimbanana.auth.service;

import io.wisoft.kimbanana.auth.User;
import io.wisoft.kimbanana.auth.dto.SignInRequest;
import io.wisoft.kimbanana.auth.dto.SignUpRequest;
import io.wisoft.kimbanana.auth.dto.TokenResponse;
import io.wisoft.kimbanana.auth.dto.UserInfoResponse;
import io.wisoft.kimbanana.auth.jwt.JwtTokenProvider;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.UUID;
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


    public UserInfoResponse getUserInfo(String accessToken) {
        String userId = jwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        return new UserInfoResponse(user.getId(), user.getEmail(), user.getName());

    }

    public Integer signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String id = "u_" + UUID.randomUUID();

        User user = new User(
                id,
                request.email(),
                request.name(),
                encoder.encode(request.password()),
                "kimbanana",
                "jwt_kimbanana"
        );

        return userRepository.save(user);
    }

    public TokenResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh 토큰이 유효하지 않음");
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

        return new TokenResponse(newAccessToken, refreshToken); // 기존 refresh 재사용
    }
}
