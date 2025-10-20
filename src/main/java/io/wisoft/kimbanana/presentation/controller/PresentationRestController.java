package io.wisoft.kimbanana.presentation.controller;

import io.wisoft.kimbanana.presentation.dto.response.SlideWrapper;
import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class PresentationRestController {

    private final PresentationService presentationService;

    @GetMapping("/{presentation-id}/slides")
    public ResponseEntity<Presentation> getSlides(@PathVariable("presentation-id") String presentationId) {
        Presentation presentation = presentationService.findByPresentationId(presentationId);

        return ResponseEntity.ok(presentation);
    }

    @GetMapping("/{presentation-id}/slides/{slide-id}")
    public ResponseEntity<SlideWrapper> getSlide(@PathVariable("presentation-id") String presentationId,
                                                 @PathVariable("slide-id") String slideId) {

        Slide slide = presentationService.getSlide(presentationId, slideId);
        return ResponseEntity.ok(new SlideWrapper(slide));
    }

    @PostMapping("/{presentation-id}/slides")
    public ResponseEntity<SlideAddPayload> createSlide(@PathVariable("presentation-id") String presentationId) {
        SlideAddPayload slide = presentationService.addSlide(presentationId);
        return ResponseEntity.ok(slide);
    }

    @PatchMapping("/{presentation-id}/slides")
    public ResponseEntity<Integer> updateStructure(@PathVariable("presentation-id") String presentationId,
                                                   @RequestBody StructurePayload structurePayload) {

        int result = presentationService.updateStruct(presentationId, structurePayload);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{presentation-id}/slides/title")
    public ResponseEntity<?> updateTitle(@PathVariable("presentation-id") String presentationId,
                                         @RequestBody TitlePayload payload) {

        presentationService.updateTitle(presentationId, payload.getNewTitle());
        return ResponseEntity.noContent().build();
    }
}
