package io.wisoft.kimbanana.presentation.controller;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import io.wisoft.kimbanana.presentation.util.StructureConvert;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PresentationWSController {
    private final PresentationService presentationService;

    @MessageMapping("/slide.edit.presentation.{presentationId}.slide.{currentSlideId}")
    public void editSlide(@DestinationVariable String presentationId,
                          @DestinationVariable String currentSlideId,
                          @Payload Slide slide) {

        log.info("수정 요청 슬라이드: {}", slide.getSlideId());

        presentationService.updateSlide(presentationId, currentSlideId, slide);
    }

    @MessageMapping("/slide.edit.presentation.{presentationId:[^.]+}")
    public void editSlide(@DestinationVariable String presentationId, Presentation presentation) {
        log.info("프레젠테이션 구조 편집 요청: {}", presentation.getPresentationId());

        List<SlideStructure> slideStructures = StructureConvert.structureList(presentation.getSlides());
        StructurePayload response = StructurePayload.builder()
                .presentationId(presentationId)
                .slides(slideStructures)
                .build();

        presentationService.updateStruct(presentationId, response);
    }
}



