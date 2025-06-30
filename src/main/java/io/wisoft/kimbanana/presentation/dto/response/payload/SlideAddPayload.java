package io.wisoft.kimbanana.presentation.dto.response.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlideAddPayload {
    @JsonProperty("slide_id")
    private String slideId;
    private int order;
}
