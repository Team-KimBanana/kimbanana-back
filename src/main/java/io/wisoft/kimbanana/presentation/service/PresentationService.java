package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.event.PresentationEvents.*;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepository;

    private final ApplicationEventPublisher eventPublisher;


    public Presentation findByPresentationId(final String presentationId) {

        Presentation presentation = presentationRepository.findByPresentationId(presentationId);
        List<Slide> slides = presentationRepository.findByPresentationSlides(presentationId);

        System.out.println("slides: " + slides.size() + "");

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

    @Transactional
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

        final var payload = SlideAddPayload.builder()
                .slideId(slideId)
                .order(slideOrder)
                .build();

        eventPublisher.publishEvent(new SlideAddedEvent(presentationId, payload));

        return payload;
    }

    @Transactional
    public void updateTitle(final String presentationId, final String title) {
        presentationRepository.updateTitle(presentationId, title);
        eventPublisher.publishEvent(new TitleUpdatedEvent(presentationId, new TitlePayload(presentationId, title)));
    }

    @Transactional
    public void updateSlide(final String presentationId, final String slideId, final Slide slide) {
        presentationRepository.updateSlide(presentationId, slideId, slide);
        eventPublisher.publishEvent(new SlideUpdatedEvent(presentationId, slideId, slide));
    }

    @Transactional
    public int updateStruct(final String presentationId, final StructurePayload slideStructures) {
        int result = presentationRepository.updateStruct(presentationId, slideStructures.getSlides());
        eventPublisher.publishEvent(new StructureUpdatedEvent(presentationId, slideStructures));
        return result;
    }


    private int getSlideOrder(final String presentationId) {
        int orderCount = presentationRepository.getSlideOrder(presentationId);
        return orderCount;
    }
}

