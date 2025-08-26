package io.wisoft.kimbanana.image.repository;

import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;


public class ImageUploadRepository {

    private final JdbcTemplate jdbcTemplate;

    public ImageUploadRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void savePresentationThumbnail(final String responseUrl, final String presentationId) {
        String sql = "UPDATE presentation_thumbnail SET thumbnail_url = ? WHERE presentation_id = ?";
        int update = jdbcTemplate.update(sql, responseUrl, presentationId);

        if (update == 0) {
            jdbcTemplate.update("INSERT INTO presentation_thumbnail (id, presentation_id, thumbnail_url) VALUES (?, ?, ?)",
                    "t" + UUID.randomUUID(), presentationId, responseUrl);
        }
    }
}
