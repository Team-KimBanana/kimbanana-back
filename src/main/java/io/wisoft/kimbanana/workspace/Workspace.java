package io.wisoft.kimbanana.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Workspace {
    private Presentation presentation;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    public Workspace(final Presentation presentation, final String thumbnailUrl) {
        this.presentation = presentation;
        this.thumbnailUrl = thumbnailUrl;
    }
}