package io.wisoft.kimbanana.presentation.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slide {
    private String slideId;
    private LocalDateTime lastRevisionDate;
    private String lastRevisionUserId;
    private int slideOrder;
    private Object data;
}
