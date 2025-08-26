package io.wisoft.kimbanana.history.controller;

import io.wisoft.kimbanana.history.History;
import io.wisoft.kimbanana.history.HistoryListResponse;
import io.wisoft.kimbanana.history.RestorePayload;
import io.wisoft.kimbanana.history.SavePayload;
import io.wisoft.kimbanana.history.repository.HistoryRepository;
import io.wisoft.kimbanana.history.service.HistoryService;
import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class HistoryController {

    private final HistoryService historyService;

    //전체 히스토리 목록 조회
    @GetMapping("/{presentation-id}/histories")
    public List<HistoryListResponse> getHistoryList(@PathVariable("presentation-id") String presentationId) {
        return historyService.findByPresentationId(presentationId);
    }

    //단일 히스토리 조회
    @GetMapping("/{presentation-id}/histories/{history-id}")
    public List<History> getHistories(@PathVariable("history-id") String historyId) {
        return historyService.findByHistoryId(historyId);
    }

    //히스토리 저장
    @PostMapping("/{presentation-id}/histories")
    public ResponseEntity<String> createHistory(@PathVariable("presentation-id") String presentationId, @RequestBody SavePayload request) {
        String historyId = historyService.addHistory(presentationId, request.getSlides(), request.getCurrentUserId());
        return ResponseEntity.ok(historyId);
    }

    //히스토리 복원
    @PostMapping("/{presentation-id}/restorations")
    public ResponseEntity<HttpStatus> restoreHistory(@PathVariable("presentation-id") String presentationId, @RequestBody RestorePayload payload) {
        historyService.restoreHistory(presentationId, payload);
        return ResponseEntity.ok(HttpStatus.valueOf(303));
    }

}