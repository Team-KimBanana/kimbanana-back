package io.wisoft.kimbanana.history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDetailResponse {

    private List<SlideResponse> slides;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SlideResponse {
        @JsonProperty("slide_id")
        private String slideId;

        @JsonProperty("last_revision_date")
        private LocalDateTime lastRevisionDate;

        @JsonProperty("last_revision_user_id")
        private String lastRevisionUserId;

        private int order;

        private String data;
    }
}