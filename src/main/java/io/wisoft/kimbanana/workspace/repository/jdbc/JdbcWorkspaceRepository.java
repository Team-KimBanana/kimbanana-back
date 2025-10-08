package io.wisoft.kimbanana.workspace.repository.jdbc;

import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcWorkspaceRepository implements WorkspaceRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcWorkspaceRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcWorkspaceRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Workspace> findAllPresentation(String userId) {
        String sql = "SELECT p.presentation_id, p.presentation_title, p.last_revision_date, p.user_id, t.thumbnail_url FROM presentation p LEFT JOIN presentation_thumbnail t ON p.presentation_id = t.presentation_id WHERE p.user_id = ?";
        List<Workspace> presentationList = jdbcTemplate.query(sql, rowMapper(), userId);
        log.debug("presentationList: {}", presentationList);
        return jdbcTemplate.query(sql, rowMapper(), userId);
    }

    @Override
    public Workspace findPresentation(final String presentationId) {
        return null;
    }

    @Override
    public String add(final String userId) {
        String sql = "INSERT INTO presentation (presentation_id, presentation_title, last_revision_date, user_id) VALUES (?, ?, ?, ?)";

        UUID id = UUID.randomUUID();
        String title = "untitled";
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement psmt = connection.prepareStatement(sql, new String[]{"presentation_id"});
            psmt.setString(1, "p_" + id);
            psmt.setString(2, title);
            psmt.setTimestamp(3, Timestamp.valueOf(now));
            psmt.setString(4, userId);
            return psmt;
        });

        return "p_" + id;
    }

    @Override
    public int delete(final String presentationId) {
        String sql = "DELETE FROM presentation WHERE presentation_id = ?";
        return jdbcTemplate.update(sql, presentationId);
    }


    private RowMapper<Workspace> rowMapper() {
        return (rs, rowNum) -> {

            Presentation presentation = Presentation.builder()
                    .presentationId(rs.getString("presentation_id"))
                    .presentationTitle(rs.getString("presentation_title"))
                    .lastRevisionDate(rs.getTimestamp("last_revision_date").toLocalDateTime())
                    .userId(rs.getString("user_id"))
                    .build();

            String thumbnailUrl = rs.getString("thumbnail_url");

            return new Workspace(presentation, thumbnailUrl);
        };
    }
}

