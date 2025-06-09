package io.wisoft.kimbanana.workspace.controller;

import java.util.List;
import io.wisoft.kimbanana.workspace.Presentation;
import io.wisoft.kimbanana.workspace.service.PresentationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspace/presentations")
public class PresentationController {

    private final PresentationService presentationService;

    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping()
    public ResponseEntity<List<Presentation>> getAllSPresentation(){
        return ResponseEntity.ok(presentationService.findPresentation());
    }

    @PostMapping()
    public String addPresentation(final String userId) {
        return presentationService.addPresentation(userId);

    }

    @DeleteMapping("/{presentation-id}")
    public ResponseEntity<Void> removePresentation(@PathVariable("presentation-id") final String presentationId) {
        presentationService.deletePresentation(presentationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
