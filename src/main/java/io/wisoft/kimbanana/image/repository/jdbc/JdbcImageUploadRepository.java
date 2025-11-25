package io.wisoft.kimbanana.image.repository.jdbc;

import io.wisoft.kimbanana.image.entity.SlideImage;
import io.wisoft.kimbanana.image.repository.ImageUploadRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcImageUploadRepository implements ImageUploadRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcImageUploadRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveSlideImage(final SlideImage slideImage) {
        String sql = "INSERT INTO slide_image (id, image_url,uploaded_at, expires_at) VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(sql, slideImage.getId(), slideImage.getImageUrl(), slideImage.getUploadedAt(),
                slideImage.getExpiresAt());
    }

    public void savePresentationThumbnail(final String responseUrl, final String presentationId) {
        String sql = "UPDATE presentation_thumbnail SET thumbnail_url = ? WHERE presentation_id = ?";
        int update = jdbcTemplate.update(sql, responseUrl, presentationId);

        if (update == 0) {
            jdbcTemplate.update(
                    "INSERT INTO presentation_thumbnail (id, presentation_id, thumbnail_url) VALUES (?, ?, ?)",
                    "t" + UUID.randomUUID(), presentationId, responseUrl);
        }
    }


    @Override
    public List<SlideImage> findExpiredImages(final LocalDateTime now) {
        String sql = "SELECT id, image_url, uploaded_at, expires_at FROM slide_image WHERE expires_at < ?";

        return jdbcTemplate.query(sql, new Object[]{now},
                (rs , rowNum) -> SlideImage.builder()
                        .id(rs.getString("id"))
                        .imageUrl(rs.getString("image_url"))
                        .uploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime())
                        .expiresAt(rs.getTimestamp("expires_at").toLocalDateTime())
                        .build()
        );
    }

    @Override
    public int deleteAllBySlideIds(final List<String> slideIds) {
        if (slideIds == null || slideIds.isEmpty()) {
            return 0;
        }

        String sql = "DELETE FROM slide_image WHERE id = ?";
        int count = 0;

        for(String slideId : slideIds) {
            count += jdbcTemplate.update(sql, slideId);
        }
        return count;
    }


}
