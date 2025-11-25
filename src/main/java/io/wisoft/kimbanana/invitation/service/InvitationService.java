package io.wisoft.kimbanana.invitation.service;

import io.wisoft.kimbanana.auth.entity.GuestSession;
import io.wisoft.kimbanana.auth.repository.GuestSessionRepository;
import io.wisoft.kimbanana.infrastructure.security.jwt.JwtTokenProvider;
import io.wisoft.kimbanana.invitation.entity.InvitationToken;
import io.wisoft.kimbanana.invitation.dto.request.CreateInvitationRequest;
import io.wisoft.kimbanana.invitation.dto.response.InvitationResponse;
import io.wisoft.kimbanana.invitation.dto.response.ValidateInvitationResponse;
import io.wisoft.kimbanana.invitation.repository.InvitationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService {
    
    private final InvitationTokenRepository invitationTokenRepository;
    private final GuestSessionRepository guestSessionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Value("${app.invitation.base-url}")
    private String baseUrl;
    
    @Value("${app.invitation.default-expiration:24}")
    private int defaultExpirationHours;
    
    public InvitationResponse createInvitation(String userId, CreateInvitationRequest request) {
        int expiresInHours = request.expiresInHours() != null ? 
                request.expiresInHours() : defaultExpirationHours;
        
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(expiresInHours);
        
        InvitationToken invitationToken = InvitationToken.builder()
                .token(token)
                .presentationId(request.presentationId())
                .createdByUserId(userId)
                .expiresAt(expiresAt)
                .build();
        
        invitationTokenRepository.save(invitationToken);
        
        log.info("Created invitation token: {} for presentation: {}", token, request.presentationId());
        
        return new InvitationResponse(
                token,
                request.presentationId(),
                expiresAt,
                baseUrl + "/" + request.presentationId() + "?invite=" + token
        );
    }
    
    public ValidateInvitationResponse validateAndCreateGuestSession(String token) {
        InvitationToken invitationToken = invitationTokenRepository.findByToken(token)
                .orElse(null);
        
        if (invitationToken == null || !invitationToken.isValid()) {
            return new ValidateInvitationResponse(
                    false,
                    null,
                    null,
                    "초대 링크가 유효하지 않거나 만료되었습니다."
            );
        }
        
        String guestId = "guest-" + UUID.randomUUID();
        String displayName = "게스트";
        LocalDateTime guestExpiresAt = LocalDateTime.now().plusHours(8);
        
        GuestSession guestSession = GuestSession.builder()
                .guestId(guestId)
                .presentationId(invitationToken.getPresentationId())
                .displayName(displayName)
                .createdAt(LocalDateTime.now())
                .expiresAt(guestExpiresAt)
                .build();
        
        guestSessionRepository.save(guestSession);
        
        String guestJwtToken = jwtTokenProvider.generateGuestToken(
                guestId, invitationToken.getPresentationId(), displayName);
        
        log.info("Guest session created: {} for presentation: {}", guestId, invitationToken.getPresentationId());
        
        return new ValidateInvitationResponse(
                true,
                guestJwtToken,
                invitationToken.getPresentationId(),
                "게스트 세션이 생성되었습니다."
        );
    }
}