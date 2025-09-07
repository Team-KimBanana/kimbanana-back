package io.wisoft.kimbanana.history.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.kimbanana.history.HistoryDetailResponse;
import io.wisoft.kimbanana.history.HistoryListResponse;
import io.wisoft.kimbanana.history.RestorePayload;
import io.wisoft.kimbanana.history.SavePayload;
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

@Tag(name = "History-Controller" , description = "히스토리(복원) 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class HistoryController {

    private final HistoryService historyService;


    @Operation(summary = "전체 히스토리 목록 조회", description = "특정 프레젠테이션의 모든 히스토리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoryListResponse.class))))
    @GetMapping("/{presentation-id}/histories")
    public List<HistoryListResponse> getHistoryList(@PathVariable("presentation-id") String presentationId) {
        return historyService.findByPresentationId(presentationId);
    }

    @Operation(summary = "단일 히스토리 조회", description = "history-id를 통해 상세 히스토리 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = HistoryDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 히스토리 ID")
    })
    @GetMapping("/{presentation-id}/histories/{history-id}")
    public HistoryDetailResponse getHistories(@PathVariable("history-id") String historyId) {
        return historyService.findByHistoryId(historyId);
    }


    @Operation(summary = "히스토리 저장", description = "현재 슬라이드 상태를 히스토리에 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공 (생성된 히스토리 ID 반환)",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/{presentation-id}/histories")
    public ResponseEntity<String> createHistory(@PathVariable("presentation-id") String presentationId, @RequestBody SavePayload request) {
        String historyId = historyService.addHistory(presentationId, request.getSlides(), request.getCurrentUserId());
        return ResponseEntity.ok(historyId);
    }

    @Operation(summary = "히스토리 복원", description = "지정된 type 값에 따라 전체 또는 단일 슬라이드를 히스토리 상태로 복원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "복원 요청 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 히스토리")
    })
    @PostMapping("/{presentation-id}/restorations")
    public ResponseEntity<HttpStatus> restoreHistory(@PathVariable("presentation-id") String presentationId, @RequestBody RestorePayload payload) {

        historyService.restoreHistory(presentationId, payload);
        return ResponseEntity.ok(HttpStatus.valueOf(303));
    }
}