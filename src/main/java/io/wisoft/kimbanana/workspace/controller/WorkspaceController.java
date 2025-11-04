package io.wisoft.kimbanana.workspace.controller;

import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/workspace/presentations")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Workspace>> getAllSPresentation(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(workspaceService.findAllPresentation(userId));
    }

    @GetMapping("/{presentationId}")
    public ResponseEntity<Workspace> getSPresentation(@PathVariable String presentationId) {
        return ResponseEntity.ok(workspaceService.findPresentation(presentationId));
    }

    @PostMapping()
    public ResponseEntity<String> addPresentation(@RequestBody final Presentation request) {
        String presentationId = workspaceService.addPresentation(request.getUserId());
        return ResponseEntity.ok(presentationId);
    }

    @DeleteMapping("/{presentationId}")
    public ResponseEntity<Void> removePresentation(@PathVariable final String presentationId) {
        workspaceService.deletePresentation(presentationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}