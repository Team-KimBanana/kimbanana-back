package io.wisoft.kimbanana.history.repository.jdbc;

import io.wisoft.kimbanana.history.entity.History;
import io.wisoft.kimbanana.history.entity.Mapping;
import io.wisoft.kimbanana.history.repository.HistoryRepository;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

public class JdbcHistoryRepository implements HistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcHistoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<History> findByPresentationId(final String presentationId) {
        String sql = "SELECT * FROM history WHERE presentation_id = ?";
        List<History> history = jdbcTemplate.query(sql, rowMapper(), presentationId);
        return history;
    }

    @Override
    public List<History> findByHistoryId(final String historyId) {
        String sql = "SELECT * FROM history WHERE history_id = ?";
        List<History> history = jdbcTemplate.query(sql, rowMapper(), historyId);
        return history;
    }

    @Override
    public void addHistory(final String batchId, final String presentationId, final List<Slide> request, final String currentUserId) {
        String sql = "INSERT INTO history (history_id, last_revision_date, last_revision_user_id, slide_id, slide_order, data, presentation_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        for (Slide slide : request) {
            jdbcTemplate.update(connection -> {
                PreparedStatement psmt = connection.prepareStatement(sql);
                psmt.setString(1, batchId);
                psmt.setTimestamp(2, Timestamp.valueOf(now));
                psmt.setString(3, currentUserId);
                psmt.setString(4, slide.getSlideId());
                psmt.setInt(5, slide.getSlideOrder());
                psmt.setString(6, slide.getData().toString());
                psmt.setString(7, presentationId);
                return psmt;
            });
        }
    }

    @Transactional
    @Override
    public void overwrite(String presentationId, String batchId, String currentUserId) {
        // 1. batchId로 history 데이터 조회 (순서 보장)
        String selectSql = "SELECT data, slide_order FROM history WHERE presentation_id = ? AND history_id = ? ORDER BY slide_order ASC";
        System.out.println(batchId);
        List<Map<String, Object>> historySlides = jdbcTemplate.queryForList(selectSql, presentationId, batchId);

        if (historySlides.isEmpty()) {
            throw new IllegalArgumentException(
                    "presentation_id 에 해당하는 히스토리를 찾을 수 앖음 " + presentationId + "/history_id " + batchId
            );
        }

        // 2. 현재 presentation 슬라이드 삭제
        String deleteSql = "DELETE FROM slide WHERE presentation_id = ?";
        jdbcTemplate.update(deleteSql, presentationId);

        // 3. 새로운 slide_id 발급해서 순서대로 insert
        String insertSql = "INSERT INTO slide (slide_id, last_revision_date, data, presentation_id, slide_order, last_revision_user_id) VALUES (?, ?, ?, ?, ?, ?)";
        for (Map<String, Object> slide : historySlides) {
            String newSlideId = "s_" + UUID.randomUUID();
            jdbcTemplate.update(insertSql,
                    newSlideId,
                    Timestamp.valueOf(LocalDateTime.now()),
                    slide.get("data").toString(),
                    presentationId,
                    slide.get("slide_order"),
                    currentUserId
            );
        }
    }

    @Override
    public void restorePartialSlides(String presentationId, List<Mapping> mappings, List<Slide> slides, String currentUserId) {
        String sql = "UPDATE slide SET data = ?, last_revision_date = ?, last_revision_user_id = ? " +
                "WHERE slide_id = ? AND presentation_id = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < slides.size(); i++) {
            Slide slide = slides.get(i);
            Mapping mapping = mappings.get(i);

            batchArgs.add(new Object[]{
                    slide.getData(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    currentUserId,
                    mapping.getTargetSlide(),
                    presentationId
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public List<Slide> findHistorySlides(String historyId, List<String> slideIds) {
        if (slideIds.isEmpty()) {
            return List.of(); // 조회할 슬라이드가 없으면 빈 리스트 반환
        }
        // slideIds 개수만큼 ? 생성
        String placeholders = slideIds.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT slide_id, data, slide_order FROM history "
                + "WHERE history_id = ? AND slide_id IN (" + placeholders + ") ORDER BY slide_order ASC";

        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, historyId); // 첫 번째 ? = historyId

            // ?에 slideIds 바인딩
            for (int i = 0; i < slideIds.size(); i++) {
                ps.setString(i + 2, slideIds.get(i)); // 인덱스 2부터 시작
            }
            return ps;
        }, (rs, rowNum) -> {
            Slide slide = new Slide();
            slide.setSlideId(rs.getString("slide_id"));
            slide.setData(rs.getString("data"));
            slide.setSlideOrder(rs.getInt("slide_order"));
            return slide;
        });
    }


    public RowMapper<History> rowMapper() {
        return (rs, rowNum) -> {
            History history = History.builder()
                    .historyId(rs.getString("history_id"))
                    .lastRevisionDate(rs.getTimestamp("last_revision_date").toLocalDateTime())
                    .data(rs.getString("data"))
                    .order(rs.getInt("slide_order"))
                    .presentationId(rs.getString("presentation_id"))
                    .slideId(rs.getString("slide_id"))
                    .build();

            return history;
        };
    }
}