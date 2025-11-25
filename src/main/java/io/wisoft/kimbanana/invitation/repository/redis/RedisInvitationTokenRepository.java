package io.wisoft.kimbanana.invitation.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.kimbanana.invitation.entity.InvitationToken;
import io.wisoft.kimbanana.invitation.repository.InvitationTokenRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisInvitationTokenRepository implements InvitationTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String KEY_PREFIX = "invitation:";

    @Override
    public void save(final InvitationToken token) {
        try {
            String key  = KEY_PREFIX + token.getToken();
            String value = objectMapper.writeValueAsString(token);
            Duration ttl = Duration.between(LocalDateTime.now(), token.getExpiresAt());
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InvitationToken> findByToken(final String token) {
        try {
            String key  = KEY_PREFIX + token;
            String value = redisTemplate.opsForValue().get(key);
            if(value == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(value, InvitationToken.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(final String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }
}
