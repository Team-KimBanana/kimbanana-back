package io.wisoft.kimbanana.infrastructure.security.jwt;

import io.wisoft.kimbanana.auth.entity.GuestSession;
import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.GuestSessionRepository;
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
    private final GuestSessionRepository guestSessionRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        StompCommand cmd = accessor.getCommand();
        if (cmd == null) return message;

        if (StompCommand.CONNECT.equals(cmd)) {

            var sessionAttrs = accessor.getSessionAttributes();
            if (sessionAttrs != null) {
                var authFromHandshake = sessionAttrs.get("auth");
                if (authFromHandshake instanceof org.springframework.security.core.Authentication auth) {
                    accessor.setUser(auth);
                    ensureSessionMeta(sessionAttrs, auth);
                    log.info("[WS] CONNECT via Handshake auth. principal={}", auth.getPrincipal());
                    return message;
                }
            }

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("[WS] CONNECT rejected: Missing Authorization header and no handshake auth");
                throw new MessagingException("Missing Authorization (no cookie handshake auth)");
            }

            String token = authHeader.substring(7);
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("[WS] CONNECT rejected: invalid JWT");
                throw new MessagingException("Invalid JWT");
            }

            String userType = jwtTokenProvider.getUserType(token);
            if ("USER".equals(userType)) {
                handleUserAuthentication(accessor, token);
            } else if ("GUEST".equals(userType)) {
                handleGuestAuthentication(accessor, token);
            } else {
                log.warn("[WS] CONNECT rejected: Unknown user type {}", userType);
                throw new MessagingException("Unknown user type: " + userType);
            }
        }

        return message;
    }

    private void ensureSessionMeta(Map<String, Object> sessionAttrs, org.springframework.security.core.Authentication auth) {
        sessionAttrs.putIfAbsent("userType",
                auth.getAuthorities().stream().anyMatch(a -> "ROLE_GUEST".equals(a.getAuthority())) ? "GUEST" : "USER");
        Object principal = auth.getPrincipal();
        if (principal instanceof User u) {
            sessionAttrs.putIfAbsent("userId", u.getId());
            sessionAttrs.putIfAbsent("userName", u.getName());
        } else if (principal instanceof GuestSession g) {
            sessionAttrs.putIfAbsent("userId", g.getGuestId());
            sessionAttrs.putIfAbsent("userName", g.getDisplayName());
            sessionAttrs.putIfAbsent("presentationId", g.getPresentationId());
        } else if (principal instanceof String s) {
            sessionAttrs.putIfAbsent("userId", s);
        }
    }

    private void handleUserAuthentication(final StompHeaderAccessor accessor, final String token) {
        String userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MessagingException("User not found: " + userId));

        var authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        accessor.setUser(authentication);

        var attrs = accessor.getSessionAttributes();
        if (attrs != null) {
            attrs.put("userId", user.getId());
            attrs.put("userType", "USER");
            attrs.put("userName", user.getName());
        }

        log.info("[WS] Authenticated USER {}", user.getId());
    }

    private void handleGuestAuthentication(final StompHeaderAccessor accessor, final String token) {
        String guestId = jwtTokenProvider.getUserId(token);
        String presentationId = jwtTokenProvider.getPresentationId(token);
        String displayName = jwtTokenProvider.getDisplayName(token);

        GuestSession guestSession = guestSessionRepository.findGuestId(guestId)
                .orElseThrow(() -> new MessagingException("Guest session not found"));

        if (guestSession.isExpired()) {
            throw new MessagingException("Guest session expired");
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                guestSession, null, List.of(new SimpleGrantedAuthority("ROLE_GUEST")));

        accessor.setUser(authentication);

        var attrs = accessor.getSessionAttributes();
        if (attrs != null) {
            attrs.put("userId", guestId);
            attrs.put("userType", "GUEST");
            attrs.put("userName", displayName);
            attrs.put("presentationId", presentationId);
        }

        log.info("[WS] Authenticated GUEST {} for presentation {}", guestId, presentationId);
    }
}