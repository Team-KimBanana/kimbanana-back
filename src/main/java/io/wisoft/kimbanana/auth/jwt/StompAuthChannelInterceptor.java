package io.wisoft.kimbanana.auth.jwt;

import io.wisoft.kimbanana.auth.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        StompCommand cmd = accessor.getCommand();
        if (cmd == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(cmd)) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.info("Missing Authorization header");
                throw new MessagingException("Missing Authorization header");
            }

            String token = authHeader.substring(7);
            if (!jwtTokenProvider.validateToken(token)) {
                log.info("Invalid JWT");
                throw new MessagingException("Invalid JWT");
            }

            String userId = jwtTokenProvider.getUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new MessagingException("User not found: " + userId));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            accessor.setUser(authentication);
            accessor.setSessionAttributes(Map.of("userId", user.getId()));

            log.info("Authenticated user: {}", user.getId());
        }

        return message;
    }
}
