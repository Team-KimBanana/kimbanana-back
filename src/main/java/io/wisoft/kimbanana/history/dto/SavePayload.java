package io.wisoft.kimbanana.history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavePayload {
    @JsonProperty("last_revision_user_id")
    private String currentUserId;
    private List<Slide> slides;
}
