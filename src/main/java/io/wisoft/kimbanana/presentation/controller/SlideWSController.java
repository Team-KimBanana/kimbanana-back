package io.wisoft.kimbanana.presentation.controller;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureUpdatedResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity. Slide;
import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureUpdatedResponse;
import io.wisoft.kimbanana.presentation.repository.MockSlideRepository;
import io.wisoft.kimbanana.presentation.repository.SlideRepository;
import io.wisoft.kimbanana.presentation.util.StructureConvert;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class SlideWSController {
    SlideRepository slideRepository = new MockSlideRepository();
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/slide.edit.presentation.{presentationId}")
    public void editSlide(@DestinationVariable String presentationId, Presentation presentation) {

        List<SlideStructure> slideStructures  = StructureConvert.structureList(presentation.getSlides());
        PresentationStructureUpdatedResponse response = new PresentationStructureUpdatedResponse(presentationId,presentation.getPresentationTitle(), slideStructures);

        String topic = "/topic/presentation." + presentationId;
        messagingTemplate.convertAndSend(topic, response);
    }

    @MessageMapping("/slide.edit.presentation.{presentationId}.slide.{currentSlideId}")
    public void editSlide(@DestinationVariable String presentationId,
                          @DestinationVariable String currentSlideId,
                          Slide slide) {

        log.info("수정 요청 슬라이드: {}", slide.getSlideId());
        slideRepository.update(slide.getSlideId(), slide);

        String topic = "/topic/presentation." + presentationId + ".slide." + currentSlideId;
        messagingTemplate.convertAndSend(topic, slide);
    }
}


