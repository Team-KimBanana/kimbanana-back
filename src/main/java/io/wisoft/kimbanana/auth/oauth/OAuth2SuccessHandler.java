package io.wisoft.kimbanana.auth.oauth;

import io.wisoft.kimbanana.auth.jwt.JwtTokenProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import io.wisoft.kimbanana.auth.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException, ServletException {

        var principal = (org.springframework.security.oauth2.core.user.OAuth2User) auth.getPrincipal();
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자 조회 실패"));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        addCookie(res, "access_token", accessToken, /*maxAgeSec*/ 60 * 15);
        addCookie(res, "refresh_token", refreshToken, /*maxAgeSec*/ 60 * 60 * 24 * 14);

        res.sendRedirect("https://daisy.wisoft.io/kimbanana/ui");
    }

    private void addCookie(HttpServletResponse res, String name, String value, int maxAgeSec) {
        jakarta.servlet.http.Cookie c = new jakarta.servlet.http.Cookie(name, value);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setPath("/");
        c.setMaxAge(maxAgeSec);
        res.addHeader("Set-Cookie",
                String.format("%s=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=Strict", name, value, maxAgeSec));
    }
}