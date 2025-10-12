package io.wisoft.kimbanana.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public boolean beforeHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                                   final WebSocketHandler wsHandler,
                                   final Map<String, Object> attributes) throws Exception {

        if(request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String header = httpRequest.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                System.out.println("Bearer " + header);
                String token = header.substring(7);
                try {
                    if(jwtTokenProvider.validateToken(token)) {
                        System.out.println("JWT Token is valid");
                        String userId = jwtTokenProvider.getUserId(token);
                        attributes.put("userId", userId);
                        return true;
                    }
                }catch (Exception e) {
                    log.error("JWT validation 실패 {}" , e.getMessage());
                }
            }
        }
        log.info("webSocket HandShake rejected");
        return false;
    }

    @Override
    public void afterHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                               final WebSocketHandler wsHandler,
                               final Exception exception) {

    }
}
