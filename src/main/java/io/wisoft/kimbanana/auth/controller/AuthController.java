package io.wisoft.kimbanana.auth.controller;

import io.wisoft.kimbanana.auth.dto.request.DeleteAccountRequest;
import io.wisoft.kimbanana.auth.dto.request.SignInRequest;
import io.wisoft.kimbanana.auth.dto.request.SignUpRequest;
import io.wisoft.kimbanana.auth.dto.response.TokenResponse;
import io.wisoft.kimbanana.auth.dto.response.UserInfoResponse;
import io.wisoft.kimbanana.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9가-힣_-]{3,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/\\\\|`~]{6,}$";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getProfile(HttpServletRequest request,
                                                       @RequestHeader(value = "Authorization", required = false) String header) {
        String token = resolveToken(request, header);

        if (token == null) {
            throw new IllegalArgumentException("토큰이 없습니다. Authorization 헤더 또는 access_token 쿠키가 필요합니다.");
        }

        UserInfoResponse user = authService.getUserInfo(token);
        return ResponseEntity.ok(user);
    }

    private String resolveToken(final HttpServletRequest request, final String header) {
        // 1) Authorization: Bearer ...
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        // 2) Cookie: access_token=...
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {

        if (!request.email().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다");
        }
        if (!request.name().matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("이름이 형식이 올바르지 않습니다");
        }
        if (!request.password().matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다");
        }

        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserInfoResponse> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
        if (request.email() == null || request.password() == null) {
            throw new IllegalArgumentException("이메일과 비밀번호는 필수입니다.");
        }

        TokenResponse tokenResponse = authService.signIn(request);

        response.setHeader("Authorization", "Bearer " + tokenResponse.accessToken());
        response.setHeader("X-Refresh-Token", tokenResponse.refreshToken());

        return ResponseEntity.ok(authService.getUserInfo(tokenResponse.accessToken()));
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더는 필수입니다");
        }
        String refreshToken = header.replace("Bearer ", "");

        if (refreshToken.isEmpty()) {
            throw new IllegalArgumentException("리프레시 토큰이 비어있습니다.");
        }

        return ResponseEntity.ok(authService.refresh(refreshToken));
    }


    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request,
                                              @RequestHeader("Authorization") String header,
                                              @RequestBody DeleteAccountRequest deleteRequest) {

        String token = resolveToken(request, header);
        if(token == null) {
            throw new IllegalArgumentException("토큰이 없습니다. Authorization 헤더가 필요합니다.");
        }

        if (deleteRequest.password() == null || deleteRequest.password().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입니다");
        }

        authService.deleteAccount(token, deleteRequest.password());
        return ResponseEntity.noContent().build();
    }
}