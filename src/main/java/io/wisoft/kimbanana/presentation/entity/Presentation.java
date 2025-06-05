package io.wisoft.kimbanana.presentation.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Presentation {
    private String presentationId;
    private String presentationTitle;
    private LocalDateTime lastRevisionDate;
    private String userId;
    private List<Slide> slides;
}

