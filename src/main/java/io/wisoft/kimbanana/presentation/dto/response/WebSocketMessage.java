package io.wisoft.kimbanana.presentation.dto.response;

import io.wisoft.kimbanana.presentation.util.WebSocketMessageType;
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
public class WebSocketMessage<T> {
    private WebSocketMessageType type;
    private T payload;
}