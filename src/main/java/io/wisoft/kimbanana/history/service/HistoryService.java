package io.wisoft.kimbanana.history.service;

import io.wisoft.kimbanana.history.entity.History;
import io.wisoft.kimbanana.history.dto.HistoryDetailResponse;
import io.wisoft.kimbanana.history.dto.HistoryListResponse;
import io.wisoft.kimbanana.history.entity.Mapping;
import io.wisoft.kimbanana.history.dto.RestorePayload;
import io.wisoft.kimbanana.history.repository.HistoryRepository;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(final HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<HistoryListResponse> findByPresentationId(final String presentationId) {
        List<History> historyList = historyRepository.findByPresentationId(presentationId);
        return historyList.stream()
                .map(h -> new HistoryListResponse(h.getHistoryId(), h.getLastRevisionDate()))
                .collect(Collectors.toList());
    }

    public HistoryDetailResponse findByHistoryId(final String historyId) {
        List<History> historyDetailList = historyRepository.findByHistoryId(historyId);

        List<HistoryDetailResponse.SlideResponse> slides = historyDetailList.stream()
                .map(history -> HistoryDetailResponse.SlideResponse.builder()
                        .slideId(history.getSlideId())
                        .lastRevisionDate(history.getLastRevisionDate())
                        .lastRevisionUserId(history.getPresentationId())
                        .order(history.getOrder())
                        .data(history.getData())
                        .build()
                )
                .toList();

        return HistoryDetailResponse.builder()
                .slides(slides)
                .build();
    }

    public String addHistory(final String presentationId, final List<Slide> slides, final String currentUserId) {
        // service에서 batchId(UUID) 생성
        String batchId = "h_" + UUID.randomUUID();
        historyRepository.addHistory(batchId, presentationId, slides, currentUserId);
        return batchId;
    }

    @Transactional
    public HttpStatus restoreHistory(final String presentationId, final RestorePayload request) {
        List<History> history = historyRepository.findByHistoryId(request.getHistoryId());
        if (history == null) {
            return HttpStatus.NOT_FOUND;
        }

        switch (request.getType()) {
            case "all":
                historyRepository.overwrite(presentationId, request.getHistoryId(), request.getCurrentUserId());
                break;
            case "partial":
                List<String> historySlideIds = request.getMappings().stream()
                        .map(Mapping::getHistorySlide)
                        .collect(Collectors.toList());

                List<Slide> slidesToRestore = historyRepository.findHistorySlides(request.getHistoryId(),
                        historySlideIds);

                // 2. Repository에서 batch update
                historyRepository.restorePartialSlides(presentationId, request.getMappings(), slidesToRestore,
                        request.getCurrentUserId());
                break;
            default:
                return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}