package io.wisoft.kimbanana.workspace.controller;

import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspace/presentations")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping()
    public ResponseEntity<List<Workspace>> getAllSPresentation(){
        return ResponseEntity.ok(workspaceService.findAllPresentation());
    }

    @PostMapping()
    public String addPresentation(final String userId) {
        return workspaceService.addPresentation(userId);

    }

    @DeleteMapping("/{presentation-id}")
    public ResponseEntity<Void> removePresentation(@PathVariable("presentation-id") final String presentationId) {
        workspaceService.deletePresentation(presentationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}