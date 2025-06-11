package io.wisoft.kimbanana.workspace;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Workspace {
    private String presentationId;
    private String presentationTitle;
    private LocalDateTime lastRevisionDate;
    private String userId;
//    private List<Slide> slides;
}


