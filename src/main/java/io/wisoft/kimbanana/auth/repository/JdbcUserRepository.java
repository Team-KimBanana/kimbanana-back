package io.wisoft.kimbanana.auth.repository;

import io.wisoft.kimbanana.auth.User;
import java.util.Optional;
import java.util.UUID;
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
                    .id(rs.getString("id"))
                    .email(rs.getString("email"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .build(), email);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer save(final User user) {
        String sql = "INSERT INTO users (id, email, name, password, provider, provider_id) VALUES (?, ?, ?, ?, ?, ?)";

        String id = "u_" + UUID.randomUUID();

        return jdbcTemplate.update(sql,id,  user.getEmail(), user.getName(), user.getPassword(), user.getProvider(), user.getProviderId());
    }
}
