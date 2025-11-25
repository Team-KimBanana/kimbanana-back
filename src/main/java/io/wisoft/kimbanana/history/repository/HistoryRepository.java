package io.wisoft.kimbanana.history.repository;

import io.wisoft.kimbanana.history.entity.History;
import io.wisoft.kimbanana.history.entity.Mapping;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.List;

public interface HistoryRepository {

    List<History> findByPresentationId(final String presentationId);

    List<History> findByHistoryId(final String historyId);

    void addHistory(final String batchId, final String presentationId, final List<Slide> request, final String currentUserId);

    void overwrite(final String presentationId, final String contents, final String currentUserId);

    List<Slide> findHistorySlides(String historyId, List<String> slideIds);

    void restorePartialSlides(String presentationId, List<Mapping> mappings, List<Slide> slidesToRestore, final String currentUserId);
}