package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import java.util.List;

public class PresentationService {

    private PresentationRepository presentationRepository;

    public PresentationService(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    public Presentation findByPresentationId(final String presentationId) {
        return presentationRepository.findByPresentationId(presentationId);
    }

    public Slide getSlide(final String presentationId, final String slideId) {
        return presentationRepository.findByIdSlide(presentationId, slideId);
    }

    public SlideAddResponse addSlide(String presentationId) {
        SlideAddResponse slideInfo = presentationRepository.createSlide(presentationId);
        return slideInfo;
    }

    public int deleteSlide(final String presentationId, final String slideId) {
        return presentationRepository.deleteSlide(presentationId, slideId);
    }

    public void updateSlide(final String presentationId, final String slideId, final Slide slide) {
        presentationRepository.updateSlide(presentationId, slideId, slide);
    }

    public void updateStruct(final String presentationId, final List<SlideStructure> slideStructures) {
        presentationRepository.updateStruct(presentationId, slideStructures);
    }
}
