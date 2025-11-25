package io.wisoft.kimbanana.invitation.repository;

import io.wisoft.kimbanana.invitation.entity.InvitationToken;
import java.util.Optional;

public interface InvitationTokenRepository {
    void save(InvitationToken token);
    Optional<InvitationToken> findByToken(final String token);
    void delete(String token);
}
