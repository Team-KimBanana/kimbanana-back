package io.wisoft.kimbanana.invitation.service;

import io.wisoft.kimbanana.auth.entity.GuestSession;
import io.wisoft.kimbanana.auth.service.GuestSessionService;
import io.wisoft.kimbanana.invitation.dto.request.CreateInvitationRequest;
import io.wisoft.kimbanana.invitation.dto.response.InvitationResponse;
import io.wisoft.kimbanana.invitation.dto.response.ValidateInvitationResponse;
import io.wisoft.kimbanana.invitation.entity.InvitationToken;
import io.wisoft.kimbanana.invitation.repository.InvitationTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationTokenRepository invitationTokenRepository;
    private final GuestSessionService questSessionService;

    @Value("${app.invitation.base-url}")
    private String baseUrl;

    @Value("${app.invitation.default-expiration:24}")
    private int defaultExpirationHours;

    /***
     * 초대 토큰 생성
     */
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

    /**
     * 초대 검증 및 게스트 세션 생성
     */
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

        GuestSession guestSession = questSessionService.createGuestSession(invitationToken.getPresentationId());
        String guestJwtToken = questSessionService.generateGuestToken(guestSession);

        return new ValidateInvitationResponse(
                true,
                guestJwtToken,
                invitationToken.getPresentationId(),
                "게스트 세션이 생성되었습니다."
        );
    }
}