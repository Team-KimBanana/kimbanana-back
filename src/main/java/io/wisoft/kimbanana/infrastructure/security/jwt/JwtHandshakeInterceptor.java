package io.wisoft.kimbanana.infrastructure.security.jwt;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servlet) {
            var req = servlet.getServletRequest();
            var cookies = req.getCookies();
            if (cookies != null) {
                for (var c : cookies) {
                    if ("access_token".equals(c.getName())) {
                        var token = c.getValue();
                        if (jwtTokenProvider.validateToken(token)) {
                            var userId = jwtTokenProvider.getUserId(token);
                            var role = "USER".equals(jwtTokenProvider.getUserType(token)) ? "ROLE_USER" : "ROLE_GUEST";
                            var auth = new UsernamePasswordAuthenticationToken(
                                    userId, null, List.of(new SimpleGrantedAuthority(role)));
                            attributes.put("auth", auth);
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res,
                                         WebSocketHandler wsHandler, Exception ex) { }
}
