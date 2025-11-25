package io.wisoft.kimbanana.auth.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.kimbanana.auth.entity.GuestSession;
import io.wisoft.kimbanana.auth.repository.GuestSessionRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisGuestSessionRepository implements GuestSessionRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String KEY_PREFIX = "guest:session:";

    @Override
    public void save(final GuestSession session) {
        try {
            String key = KEY_PREFIX + session.getGuestId();
            String value = objectMapper.writeValueAsString(session);
            Duration ttl = Duration.between(LocalDateTime.now(), session.getExpiresAt());
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<GuestSession> findGuestId(final String guestId) {
        try {
            String key = KEY_PREFIX + guestId;
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(value, GuestSession.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize guest session", e);
        }
    }

    @Override
    public void delete(final String guestId) {
        redisTemplate.delete(KEY_PREFIX + guestId);
    }
}
