package io.wisoft.kimbanana.presentation.repository;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload.SlideStructure;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.List;


public interface PresentationRepository {
    int createSlide(String presentationId, Slide slide);

    Presentation findByPresentationId(String presentationId);

    List<Slide> findByPresentationSlides(String presentationId);

    Slide findByIdSlide(String presentationId, String slideId);

    void updateTitle(String presentationId, String title);

    void updateSlide(String presentationId, String slideId, Slide slide);

    int updateStruct(String presentationId, List<SlideStructure> slideStructures);

    int getSlideOrder(String presentationId);

    void updatePresentation(String presentationId, TitlePayload titlePayload);
}
