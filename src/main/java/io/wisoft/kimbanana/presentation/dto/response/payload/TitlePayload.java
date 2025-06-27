package io.wisoft.kimbanana.presentation.dto.response.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitlePayload {
    @JsonProperty("presentation_id")
    private String presentationId;

    @JsonProperty("new_title")
    private String newTitle;
}