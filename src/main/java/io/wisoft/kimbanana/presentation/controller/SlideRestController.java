package io.wisoft.kimbanana.presentation.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureUpdatedResponse;
import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureUpdatedResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import io.wisoft.kimbanana.presentation.util.StructureConvert;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class SlideRestController {

    private final PresentationService presentationService;
    private final SimpMessagingTemplate messagingTemplate;

    // 1. 슬라이드 추가
    @PostMapping("/{presentationId}/slides")
    public ResponseEntity<SlideAddResponse> createSlide(@PathVariable("presentationId") String presentationId) {
        SlideAddResponse slide = presentationService.addSlide(presentationId);
        Presentation presentation = presentationService.findByPresentationId(presentationId);

        List<SlideStructure> slideList = StructureConvert.structureList(presentation.getSlides());

        PresentationStructureUpdatedResponse response = new PresentationStructureUpdatedResponse(
                presentation.getPresentationId(), presentation.getPresentationTitle(), slideList);

        // 실시간 알림: 새 슬라이드 추가됨
        messagingTemplate.convertAndSend(
                "/topic/presentation." + presentationId, response
        );

        return ResponseEntity.ok(slide);
    }

}
