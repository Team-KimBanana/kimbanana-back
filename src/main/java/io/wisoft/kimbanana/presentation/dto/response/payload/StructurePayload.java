package io.wisoft.kimbanana.presentation.dto.response.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StructurePayload {
    @JsonProperty("presentation_id")
    private String presentationId;
    private List<SlideStructure> slides;

    @Builder
    @Getter
    public static class SlideStructure {
        @JsonProperty("slide_id")
        private String slideId;
        private int order;
    }
}