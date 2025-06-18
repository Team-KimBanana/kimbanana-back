package io.wisoft.kimbanana.presentation.controller;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse;
import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.dto.response.SlideWrapper;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import io.wisoft.kimbanana.presentation.util.StructureConvert;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class PresentationRestController {

    private final PresentationService presentationService;
    private final SimpMessagingTemplate messagingTemplate;

    //단일 슬라이드 조회
    @GetMapping("/{presentation-id}/slides/{slide-id}")
    public ResponseEntity<SlideWrapper> getSlide(@PathVariable("presentation-id") String presentationId,
                                          @PathVariable("slide-id") String slideId) {

        Slide slide = presentationService.getSlide(presentationId, slideId);
        System.out.println(slide.getSlideId());

        return ResponseEntity.ok(new SlideWrapper(slide));
    }


    // 슬라이드 추가
    @PostMapping("/{presentation-id}/slides")
    public ResponseEntity<SlideAddResponse> createSlide(@PathVariable("presentation-id") String presentationId) {
        SlideAddResponse slide = presentationService.addSlide(presentationId);
        Presentation presentation = presentationService.findByPresentationId(presentationId);

        List<SlideStructure> slideList = StructureConvert.structureList(presentation.getSlides());

        PresentationStructureResponse response = new PresentationStructureResponse(
                presentation.getPresentationId(), presentation.getPresentationTitle(), slideList);

        // 실시간 알림: 새 슬라이드 추가됨
        messagingTemplate.convertAndSend(
                "/topic/presentation." + presentationId, response
        );

        return ResponseEntity.ok(slide);
    }


    // 슬라이드 삭제
    @DeleteMapping("/{presentation-id}/slides/{slide-id}")
    public ResponseEntity<?> deleteSlide(@PathVariable("presentation-id") String presentationId,
                                         @PathVariable("slide-id") String slideId) {

        presentationService.deleteSlide(presentationId, slideId);

        Presentation presentation = presentationService.findByPresentationId(presentationId);
        List<SlideStructure> slideList = StructureConvert.structureList(presentation.getSlides());

        PresentationStructureResponse response = new PresentationStructureResponse(
                presentation.getPresentationId(), presentation.getPresentationTitle(), slideList);

        // 실시간 알림: 슬라이드 삭제됨
        messagingTemplate.convertAndSend(
                "/topic/presentation." + presentationId, response
        );

        return ResponseEntity.noContent().build();
    }
}