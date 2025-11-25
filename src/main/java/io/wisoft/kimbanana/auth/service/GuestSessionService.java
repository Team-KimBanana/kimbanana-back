package io.wisoft.kimbanana.auth.service;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.wisoft.kimbanana.auth.entity.GuestSession;
import io.wisoft.kimbanana.auth.repository.GuestSessionRepository;
import io.wisoft.kimbanana.infrastructure.security.jwt.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestSessionService {

    private final GuestSessionRepository guestSessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 게스트 세션 생성
     */
    public GuestSession createGuestSession(String presentationId) {
        String guestId = "G_" + UUID.randomUUID();
        String displayName = "게스트";
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(8);

        GuestSession guestSession = GuestSession.builder()
                .guestId(guestId)
                .presentationId(presentationId)
                .displayName(displayName)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .build();

        guestSessionRepository.save(guestSession);

        log.info("Created guest session with id {}", guestSession.getGuestId());

        return guestSession;
    }

    /**
     * 게스트 JWT 토큰 생성
     */
    public String generateGuestToken(GuestSession guestSession) {
        return jwtTokenProvider.generateGuestToken(
                guestSession.getGuestId(),
                guestSession.getPresentationId(),
                guestSession.getDisplayName()
        );
    }

    /**
     * 게스트 세션 조회
     */
    public GuestSession findById(String guestId) {
        return guestSessionRepository.findGuestId(guestId)
                .orElseThrow(() -> new IllegalArgumentException("게스트 세션을 찾을 수 없습니다"));
    }

    /**
     * 게스트 세션 삭제
     */
    public void deleteSession(String guestId) {
        guestSessionRepository.delete(guestId);
    }

    /**
     * 게스트 세션 만료 여부 확인
     */
    public boolean isSessionExpired(GuestSession guestSession) {
        return LocalDateTime.now().isAfter(guestSession.getExpiresAt());
    }
}
