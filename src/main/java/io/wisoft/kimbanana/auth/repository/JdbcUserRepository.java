package io.wisoft.kimbanana.auth.repository;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.wisoft.kimbanana.auth.User;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .provider(rs.getString("provider"))
                    .providerId(rs.getString("providerId"))
                    .build(), email);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer save(final User user) {
        String sql = "INSERT INTO users (email, name, password, provider, provider_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getPassword(), user.getProvider(), user.getProviderId());
    }
}
