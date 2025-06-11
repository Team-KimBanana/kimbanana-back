package io.wisoft.kimbanana.presentation.dto.response;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationStructureResponse {
    private String presentationId;
    private String title;
    private List<SlideStructure> slides;

    public PresentationStructureResponse() {
    }

    public PresentationStructureResponse(String presentationId, String title, List<SlideStructure> slides) {
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