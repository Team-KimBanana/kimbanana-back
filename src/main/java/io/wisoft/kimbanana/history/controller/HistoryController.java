package io.wisoft.kimbanana.history.controller;

import io.wisoft.kimbanana.history.dto.HistoryDetailResponse;
import io.wisoft.kimbanana.history.dto.HistoryListResponse;
import io.wisoft.kimbanana.history.dto.RestorePayload;
import io.wisoft.kimbanana.history.dto.SavePayload;
import io.wisoft.kimbanana.history.service.HistoryService;
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

    @GetMapping("/{presentation-id}/histories")
    public List<HistoryListResponse> getHistoryList(@PathVariable("presentation-id") String presentationId) {
        return historyService.findByPresentationId(presentationId);
    }

    @GetMapping("/{presentation-id}/histories/{history-id}")
    public HistoryDetailResponse getHistories(@PathVariable("history-id") String historyId) {
        return historyService.findByHistoryId(historyId);
    }

    @PostMapping("/{presentation-id}/histories")
    public ResponseEntity<String> createHistory(@PathVariable("presentation-id") String presentationId, @RequestBody SavePayload request) {
        String historyId = historyService.addHistory(presentationId, request.getSlides(), request.getCurrentUserId());
        return ResponseEntity.ok(historyId);
    }

    @PostMapping("/{presentation-id}/restorations")
    public ResponseEntity<HttpStatus> restoreHistory(@PathVariable("presentation-id") String presentationId, @RequestBody RestorePayload payload) {

        historyService.restoreHistory(presentationId, payload);
        return ResponseEntity.ok(HttpStatus.valueOf(303));
    }
}