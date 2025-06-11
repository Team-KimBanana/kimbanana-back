package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;

public class PresentationService {

    private PresentationRepository presentationRepository;

    public PresentationService(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }


    public SlideAddResponse addSlide(String presentationId) {
        SlideAddResponse slideInfo = presentationRepository.createSlide(presentationId);
        return slideInfo;
    }

    public Presentation findByPresentationId(final String presentationId) {
        return presentationRepository.findByPresentationId(presentationId);
    }
}
