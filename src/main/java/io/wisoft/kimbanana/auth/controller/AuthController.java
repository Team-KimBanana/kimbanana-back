package io.wisoft.kimbanana.auth.controller;

import io.wisoft.kimbanana.auth.dto.SignInRequest;
import io.wisoft.kimbanana.auth.dto.SignUpRequest;
import io.wisoft.kimbanana.auth.dto.TokenResponse;
import io.wisoft.kimbanana.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "[a-zA-Z0-9_-]{3,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {

        if(!request.getEmail().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다");
        }
        if (!request.getName().matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("이름이 형식이 올바르지 않습니다");
        }
        if (!request.getPassword().matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다");
        }

        try {
            authService.signUp(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
        if(request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("이메일과 비밀번호는 필수입니다.");
        }

        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String header) {
        if(header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더는 필수입니다");
        }
        String refreshToken = header.replace("Bearer ", "");

        if(refreshToken.isEmpty()) {
            throw new IllegalArgumentException("리프레시 토큰이 비어있습니다.");
        }

        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}