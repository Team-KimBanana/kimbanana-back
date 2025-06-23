package io.wisoft.kimbanana.workspace.repository.jdbc;

import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
import java.util.List;
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
    public List<Workspace> findAllPresentation() {
        String sql = "SELECT p.presentation_id, p.presentation_title, p.last_revision_time, p.user_id, t.thumbnail_url FROM presentation p LEFT JOIN presentation_thumbnail t ON p.presentation_id = t.presentation_id";
        List<Workspace> presentationList = jdbcTemplate.query(sql,rowMapper());
        log.debug("presentationList: {}", presentationList);
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public String add(final String userId) {
        return "";
    }


    @Override
    public int delete(final String presentationId) {
        return 0;
    }


    private RowMapper<Workspace> rowMapper() {
        return (rs, rowNum) -> {

            Presentation presentation = new Presentation(
                      rs.getString("presentation_id")
                    , rs.getString("presentation_title")
                    , rs.getTimestamp("last_revision_time").toLocalDateTime()
                    , rs.getString("user_id")
            );

            String thumbnailUrl = rs.getString("thumbnail_url");

            return new Workspace(presentation, thumbnailUrl);
        };
    }
}

