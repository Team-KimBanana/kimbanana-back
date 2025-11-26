package io.wisoft.kimbanana.auth.repository.jdbc;

import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
            User user = jdbcTemplate.queryForObject(sql, rowMapper(), email);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(final String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, rowMapper(), userId);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(final User user) {
        String sql = "INSERT INTO users (email, name, password, provider, provider_id) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "ON CONFLICT (email) "
                + "DO UPDATE "
                + "SET name = EXCLUDED.name, "
                + "provider = EXCLUDED.provider, "
                + "provider_id = EXCLUDED.provider_id "
                + "WHERE users.provider = EXCLUDED.provider";

        return jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getPassword(), user.getProvider(), user.getProviderId());
    }

    @Override
    public int deleteById(final String id) {
        String sql = "DELETE FROM users WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }


    private RowMapper<User> rowMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("provider"),
                rs.getString("provider_id")
        );
    }
}
