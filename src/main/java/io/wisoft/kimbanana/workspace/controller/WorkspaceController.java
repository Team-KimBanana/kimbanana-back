package io.wisoft.kimbanana.workspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Workspace-Controller" , description = "워크스페이스 관련 API")
@RestController
@RequestMapping("/api/workspace/presentations")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Operation(summary = "프레젠테이션 전체 목록 조회", description = "워크스페이스에 포함된 모든 프레젠테이션을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Workspace.class))))
    @GetMapping()
    public ResponseEntity<List<Workspace>> getAllSPresentation() {
        return ResponseEntity.ok(workspaceService.findAllPresentation());
    }

    @Operation(summary = "단일 프레젠테이션 조회", description = "프레젠테이션 ID로 워크스페이스의 특정 프레젠테이션을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Workspace.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프레젠테이션")
    })
    @GetMapping("/{presentationId}")
    public ResponseEntity<Workspace> getSPresentation(@PathVariable String presentationId) {
        return ResponseEntity.ok(workspaceService.findPresentation(presentationId));
    }


    @Operation(summary = "프레젠테이션 추가", description = "새로운 프레젠테이션을 워크스페이스에 추가합니다. userId를 본문으로 전달합니다.")
    @ApiResponse(responseCode = "200", description = "생성된 프레젠테이션 ID 반환",
            content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping()
    public ResponseEntity<String> addPresentation(@RequestBody final Presentation request) {
        String presentationId = workspaceService.addPresentation(request.getUserId());
        return ResponseEntity.ok(presentationId);
    }

    @Operation(summary = "프레젠테이션 삭제", description = "워크스페이스에서 특정 프레젠테이션을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공 (본문 없음)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프레젠테이션")
    })
    @DeleteMapping("/{presentationId}")
    public ResponseEntity<Void> removePresentation(@PathVariable final String presentationId) {
        workspaceService.deletePresentation(presentationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}