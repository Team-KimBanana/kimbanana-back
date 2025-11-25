package io.wisoft.kimbanana.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessValidity;
    private final long refreshValidity;
    private final long questValidity;
    private final UserRepository userRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.access-token-expiration}") long accessValidity,
                            @Value("${jwt.refresh-token-expiration}") long refreshValidity,
                            @Value("${jwt.guest-expiration}") long guestValidity,
                            final UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.accessValidity = accessValidity * 1000;
        this.refreshValidity = refreshValidity * 1000;
        this.questValidity = guestValidity * 1000;
        this.userRepository = userRepository;
    }

    //일반 사용자 accessToken 생성
    public String generateAccessToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_type", "USER");
        return createToken(userId, claims, accessValidity);
    }

    //일반 사용자 RefreshToken 생성
    public String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_type", "USER");
        return createToken(userId, claims, refreshValidity);
    }

    //게스트 token 생성
    public String generateGuestToken(String guestId, String presentationId, String displayName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_type", "GUEST");
        claims.put("guest_id", guestId);
        claims.put("presentation_id", presentationId);
        claims.put("display_name", displayName);
        return createToken(guestId, claims, questValidity);
    }


    private String createToken(String userId, Map<String, Object> claims, long validity) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    public String getUserType(String token) {
        return getClaims(token).get("user_type", String.class);
    }

    public String getPresentationId(String token) {
        return getClaims(token).get("presentation_id", String.class);
    }

    public String getDisplayName(String token) {
        return getClaims(token).get("display_name", String.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String userType = getUserType(token);

        if ("USER".equals(userType)) {
            String userId = getUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else if ("GUEST".equals(userType)) {
            String guestId = getUserId(token);
            String displayName = getDisplayName(token);

            Map<String, Object> guestInfo = Map.of(
                    "guestId", guestId,
                    "displayName", displayName,
                    "userType", "GUEST"
            );
            return new UsernamePasswordAuthenticationToken(
                    guestInfo,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_GUEST"))
            );
        }

        throw new IllegalArgumentException("유저 타입을 찾을 수 없음" + userType);
    }
}
