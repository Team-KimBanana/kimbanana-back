package io.wisoft.kimbanana.workspace.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PresentationRequest {
    @JsonProperty("user_id")
    private final String userId;
}
