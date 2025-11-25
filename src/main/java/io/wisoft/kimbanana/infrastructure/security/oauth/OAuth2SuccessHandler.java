package io.wisoft.kimbanana.infrastructure.security.oauth;

import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import io.wisoft.kimbanana.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException, ServletException {

        System.out.println(">>> [OAUTH] successHandler called");

        var principal = (org.springframework.security.oauth2.core.user.OAuth2User) auth.getPrincipal();
        System.out.println(">>> [DEBUG] principal class = " + principal.getClass().getName());
        System.out.println(">>> [DEBUG] principal attrs = " + principal.getAttributes());

        String email = principal.getAttribute("email");
        System.out.println(">>> [DEBUG] extracted email = " + email);

        if (email == null || email.isBlank()) {
            res.sendRedirect("https://daisy.wisoft.io/kimbanana/ui?oauth_error=1&reason=no-email");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OAuth2SuccessHandler: DB user not found for email " + email));

        System.out.println(">>> [DEBUG] found user id = " + user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        addCookie(res, "access_token", accessToken, 60 * 15);
        addCookie(res, "refresh_token", refreshToken, 60 * 60 * 24 * 14);

        System.out.println(">>> [OAUTH] cookies set, redirecting...");

        res.sendRedirect("https://daisy.wisoft.io/kimbanana/ui?oauth_success=1");
    }

    private void addCookie(HttpServletResponse res, String name, String value, int maxAgeSec) {
        jakarta.servlet.http.Cookie c = new jakarta.servlet.http.Cookie(name, value);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setPath("/");
        c.setMaxAge(maxAgeSec);

        res.addHeader(
                "Set-Cookie",
                String.format(
                        "%s=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=Lax",
                        name, value, maxAgeSec
                )
        );
    }
}