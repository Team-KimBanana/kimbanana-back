package io.wisoft.kimbanana.auth.service;

import io.wisoft.kimbanana.auth.dto.request.SignInRequest;
import io.wisoft.kimbanana.auth.dto.request.SignUpRequest;
import io.wisoft.kimbanana.auth.dto.response.TokenResponse;
import io.wisoft.kimbanana.auth.dto.response.UserInfoResponse;
import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    public Integer signUp(SignUpRequest request) {
        Integer result = userService.createUser(
                request.email(),
                request.name(),
                request.password()
        );
        return result;
    }

    /**
     * 로그인
     */
    public TokenResponse signIn(SignInRequest request) {

        User user = userService.findByEmail(request.email());

        if (!userService.isPasswordMatch(user, request.password())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * 토큰 갱신
     */
    public TokenResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh 토큰이 유효하지 않음");
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

        return new TokenResponse(newAccessToken, refreshToken); // 기존 refresh 재사용
    }


    /**
     * 사용자 정보 조회(토큰 기반)
     */
    public UserInfoResponse getUserInfo(String accessToken) {
        String userId = jwtTokenProvider.getUserId(accessToken);
        return userService.getUserInfo(userId);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteAccount(final String accessToken, final String password) {

        String userId = jwtTokenProvider.getUserId(accessToken);

        User user = userService.findById(userId);

        if(!userService.isPasswordMatch(user, password)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        userService.deleteUser(userId);
    }
}
