package io.wisoft.kimbanana.presentation.repository.jdbc;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload.SlideStructure;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

public class JdbcPresentationRepository implements PresentationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPresentationRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createSlide(final String presentationId, Slide slide) {
        String sql = "INSERT INTO slide (slide_id, presentation_id, last_revision_date, last_revision_user_id, slide_order, data) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(sql, slide.getSlideId(), presentationId, slide.getLastRevisionDate(),
                slide.getLastRevisionUserId(), slide.getSlideOrder(), slide.getData());
        return result;
    }

    @Override
    public Presentation findByPresentationId(final String presentationId) {
        String sql = "SELECT * FROM presentation WHERE presentation_id = ?";
        Presentation presentation = jdbcTemplate.queryForObject(sql, rowPresentationMapper(), presentationId);

        return presentation;
    }

    @Override
    public List<Slide> findByPresentationSlides(final String presentationId) {
        String sql = "SELECT * FROM slide WHERE presentation_id = ?";
        List<Slide> slides = jdbcTemplate.query(sql, rowSlideMapper(), presentationId);
        return slides;
    }


    @Override
    public Slide findByIdSlide(final String presentationId, final String slideId) {
        String sql = "SELECT * FROM slide WHERE presentation_id = ? AND slide_id = ?";
        Slide slide = jdbcTemplate.queryForObject(sql, rowSlideMapper(), presentationId, slideId);

        return slide;
    }

    @Override
    public void updateSlide(final String presentationId, final String slideId, final Slide slide) {
        String sql = "UPDATE slide \n"
                + "    SET last_revision_date = ?, last_revision_user_id = ?,slide_order = ?, data = ?\n"
                + " WHERE presentation_id  = ? AND slide_id = ?";

        jdbcTemplate.update(sql, slide.getLastRevisionDate(), slide.getLastRevisionUserId(), slide.getSlideOrder(),
                slide.getData(), presentationId, slideId);
    }

    @Override
    public void updateTitle(final String presentationId, final String title) {
        String sql = "UPDATE presentation SET presentation_title = ? WHERE presentation_id = ?";
        jdbcTemplate.update(sql, title, presentationId);
    }

    @Override
    @Transactional
    public int updateStruct(final String presentationId, final List<SlideStructure> slideStructures) {
        String selectSql = "SELECT slide_id FROM slide WHERE presentation_id = ?";
        String deleteSql = "DELETE FROM slide WHERE presentation_id = ? AND slide_id = ?";
        String updateSql = "UPDATE slide SET slide_order = ? WHERE presentation_id = ? AND slide_id = ?";

        //현재 DB에 있는 슬라이드 조회
        List<String> currentSlideIds = jdbcTemplate.queryForList(selectSql, String.class, presentationId);

        //클라이언트가 보낸 슬라이드 ID 집합
        Set<String> newSlideIds = slideStructures.stream()
                .map(SlideStructure::getSlideId)
                .collect(Collectors.toSet());

        //삭제할 슬라이드 ID 계산
        List<String> toDelete = currentSlideIds.stream()
                .filter(id -> !newSlideIds.contains(id))
                .collect(Collectors.toList());

        // 4. 삭제 실행
        for (String slideId : toDelete) {
            jdbcTemplate.update(deleteSql, presentationId, slideId);
        }

        int update = 0;
        for (SlideStructure slideStruct : slideStructures) {
            update += jdbcTemplate.update(updateSql, slideStruct.getOrder(), presentationId, slideStruct.getSlideId());
        }

        return update;
    }

    @Override
    public int getSlideOrder(final String presentationId) {
        String sql = "SELECT COUNT(slide_id) FROM slide WHERE presentation_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, presentationId);
        return count != null ? count : 0;
    }


    @Override
    public void updatePresentation(final String presentationId, final TitlePayload titlePayload) {
        String sql = "UPDATE presentation SET presentation_title = ? WHERE presentation_id = ?";
        jdbcTemplate.update(sql, titlePayload.getNewTitle(), presentationId);
    }


    public RowMapper<Presentation> rowPresentationMapper() {
        return (rs, rowMapper) -> Presentation.builder()
                .presentationId(rs.getString("presentation_id"))
                .presentationTitle(rs.getString("presentation_title"))
                .lastRevisionDate(
                        rs.getTimestamp("last_revision_date") != null ?
                                rs.getTimestamp("last_revision_date").toLocalDateTime() : null
                )
                .userId(rs.getString("user_id"))
                .build();
    }

    public RowMapper<Slide> rowSlideMapper() {
        return (rs, rowMapper) -> Slide.builder()
                .slideId(rs.getString("slide_id"))
                .lastRevisionDate(
                        rs.getTimestamp("last_revision_date") != null ?
                                rs.getTimestamp("last_revision_date").toLocalDateTime() : null
                )
                .lastRevisionUserId(rs.getString("last_revision_user_id"))
                .slideOrder(rs.getInt("slide_order"))
                .data(rs.getString("data"))
                .build();
    }
}
