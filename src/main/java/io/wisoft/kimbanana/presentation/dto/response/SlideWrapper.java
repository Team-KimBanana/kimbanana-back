package io.wisoft.kimbanana.presentation.dto.response;

import io.wisoft.kimbanana.presentation.entity.Slide;
import lombok.Builder;
import lombok.Getter;


@Getter
public class SlideWrapper {
    private Slide slide;

    @Builder
    public SlideWrapper(Slide slide) {
        this.slide = slide;
    }
}
