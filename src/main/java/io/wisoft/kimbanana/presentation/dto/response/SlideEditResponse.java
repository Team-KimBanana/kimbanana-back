package io.wisoft.kimbanana.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SlideEditResponse {
    private  String lastRevisionUserId;
    private Object data;
}
