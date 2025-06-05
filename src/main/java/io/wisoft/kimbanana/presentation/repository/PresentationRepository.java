package io.wisoft.kimbanana.presentation.repository;

import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;

public interface PresentationRepository {
    SlideAddResponse createSlide(String presentationId);
    Presentation findByPresentationId(String presentationId);
}
