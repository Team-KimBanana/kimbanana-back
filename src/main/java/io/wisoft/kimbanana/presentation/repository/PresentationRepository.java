package io.wisoft.kimbanana.presentation.repository;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.List;

public interface PresentationRepository {
    SlideAddResponse createSlide(String presentationId);
    Presentation findByPresentationId(String presentationId);
    int deleteSlide(String presentationId, String slideId);
    Slide findByIdSlide(String presentationId, String slideId);
    void updateSlide(String presentationId, String slideId, Slide slide);
    void updateStruct(String presentationId, List<SlideStructure> slideStructures);
}

