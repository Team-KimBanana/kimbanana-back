package io.wisoft.kimbanana.auth.repository;

import io.wisoft.kimbanana.auth.GuestSession;
import java.util.Optional;

public interface GuestSessionRepository {
    void save(GuestSession guestSession);
    Optional<GuestSession> findGuestId(String guestId);
    void delete(String guestId);
}


