package io.wisoft.kimbanana.presentation.dto.response;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationStructureUpdatedResponse {
    private String presentationId;
    private String title;
    private List<SlideStructure> slides;

    public PresentationStructureUpdatedResponse() {
    }

    public PresentationStructureUpdatedResponse(String presentationId, String title, List<SlideStructure> slides) {
        this.presentationId = presentationId;
        this.title = title;
        this.slides = slides;
    }

    @Getter
    @Setter
    public static class SlideStructure {
        private String slideId;
        private int order;
    }
}