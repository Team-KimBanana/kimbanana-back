package io.wisoft.kimbanana.presentation.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slide {
    @JsonProperty("slide_id")
    private String slideId;

    @JsonProperty("last_revision_date")
    private LocalDateTime lastRevisionDate;

    @JsonProperty("last_revision_user_id")
    private String lastRevisionUserId;

    @JsonProperty("order")
    private int slideOrder;

    private String data;
}
