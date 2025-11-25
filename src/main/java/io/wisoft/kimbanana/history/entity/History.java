package io.wisoft.kimbanana.history.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class History {
    @JsonProperty("history_id")
    private String historyId;

    @JsonProperty("last_revision_date")
    private LocalDateTime lastRevisionDate;

    @JsonProperty("slide_id")
    private String slideId;

    private int order;

    @JsonProperty("presentation_id")
    private String presentationId;

    private String data;
}
