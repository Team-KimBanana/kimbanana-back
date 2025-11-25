package io.wisoft.kimbanana.invitation.controller;

import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.invitation.dto.request.CreateInvitationRequest;
import io.wisoft.kimbanana.invitation.dto.response.InvitationResponse;
import io.wisoft.kimbanana.invitation.dto.response.ValidateInvitationResponse;
import io.wisoft.kimbanana.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    
    private final InvitationService invitationService;
    
    @PostMapping
    public ResponseEntity<InvitationResponse> createInvitation(
            @AuthenticationPrincipal User user,
            @RequestBody CreateInvitationRequest request) {
        
        InvitationResponse response = invitationService.createInvitation(user.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{token}")
    public ResponseEntity<ValidateInvitationResponse> validateInvitation(
            @PathVariable String token) {
        
        ValidateInvitationResponse response = invitationService.validateAndCreateGuestSession(token);
        return ResponseEntity.ok(response);
    }
}