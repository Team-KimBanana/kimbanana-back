package io.wisoft.kimbanana.presentation.repository.jdbc;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcPresentationRepository implements PresentationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPresentationRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public SlideAddResponse createSlide(final String presentationId) {
        return null;
    }

    @Override
    public Presentation findByPresentationId(final String presentationId) {
        return null;
    }

    @Override
    public int deleteSlide(final String presentationId, final String slideId) {
        return 0;
    }

    @Override
    public Slide findByIdSlide(final String presentationId, final String slideId) {
        return null;
    }

    @Override
    public void updateSlide(final String presentationId, final String slideId, final Slide slide) {

    }

    @Override
    public void updateStruct(final String presentationId, final List<SlideStructure> slideStructures) {

    }
}
