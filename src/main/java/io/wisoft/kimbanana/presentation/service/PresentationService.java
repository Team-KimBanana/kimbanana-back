package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PresentationService {

    private final PresentationRepository presentationRepository;

    public PresentationService(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    public Presentation findByPresentationId(final String presentationId) {

        Presentation presentation = presentationRepository.findByPresentationId(presentationId);
        List<Slide> slides = presentationRepository.findByPresentationSlides(presentationId);

        presentation = Presentation.builder()
                .presentationId(presentation.getPresentationId())
                .presentationTitle(presentation.getPresentationTitle())
                .lastRevisionDate(presentation.getLastRevisionDate())
                .userId(presentation.getUserId())
                .slides(slides)
                .build();

        return presentation;
    }

    public Slide getSlide(final String presentationId, final String slideId) {
        return presentationRepository.findByIdSlide(presentationId, slideId);
    }

    public SlideAddPayload addSlide(String presentationId) {
        String slideId = "s_" + UUID.randomUUID();
        int slideOrder = getSlideOrder(presentationId) + 1;
        LocalDateTime timestamp = LocalDateTime.now();

        Slide slide = Slide.builder()
                .slideId(slideId)
                .lastRevisionDate(timestamp)
                .slideOrder(slideOrder)
                .data("{}")
                .build();

        int result = presentationRepository.createSlide(presentationId, slide);

        if (result != 1) {
            throw new RuntimeException("슬라이드 생성 실패");
        }

        return SlideAddPayload.builder()
                .slideId(slideId)
                .order(slideOrder)
                .build();
    }

    public void updateTitle(final String presentationId, final String title) {
        presentationRepository.updateTitle(presentationId, title);
    }

    public void updateSlide(final String presentationId, final String slideId, final Slide slide) {
        presentationRepository.updateSlide(presentationId, slideId, slide);
    }

    public int updateStruct(final String presentationId, final StructurePayload slideStructures) {
        return presentationRepository.updateStruct(presentationId, slideStructures.getSlides());
    }


    private int getSlideOrder(final String presentationId) {
        int orderCount = presentationRepository.getSlideOrder(presentationId);
        return orderCount;
    }
}

