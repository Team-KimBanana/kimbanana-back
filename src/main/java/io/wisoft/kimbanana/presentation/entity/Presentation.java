package io.wisoft.kimbanana.presentation.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class Presentation {
    @JsonProperty("presentation_id")
    private String presentationId;

    @JsonProperty("presentation_title")
    private String presentationTitle;

    @JsonProperty("last_revision_date")
    private LocalDateTime lastRevisionDate;

    @JsonProperty("user_id")
    private String userId;

    private List<Slide> slides;
}

