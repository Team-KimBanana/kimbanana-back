
package io.wisoft.kimbanana.presentation.listener;

import io.wisoft.kimbanana.presentation.dto.response.SlideEditResponse;
import io.wisoft.kimbanana.presentation.dto.response.WebSocketMessage;
import io.wisoft.kimbanana.presentation.event.PresentationEvents.*;
import io.wisoft.kimbanana.presentation.util.WebSocketMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresentationEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSlideAdded(final SlideAddedEvent event) {
        final String destination = "/topic/presentation." + event.presentationId();
        final var message = WebSocketMessage.builder()
                .type(WebSocketMessageType.SLIDE_ADD)
                .payload(event.payload())
                .build();
        messagingTemplate.convertAndSend(destination, message);
    }

    @EventListener
    public void handleStructureUpdated(final StructureUpdatedEvent event) {
        final String destination = "/topic/presentation." + event.presentationId();
        final var message = WebSocketMessage.builder()
                .type(WebSocketMessageType.STRUCTURE_UPDATED)
                .payload(event.payload())
                .build();
        messagingTemplate.convertAndSend(destination, message);
    }

    @EventListener
    public void handleTitleUpdated(final TitleUpdatedEvent event) {
        final String destination = "/topic/presentation." + event.presentationId();
        final var message = WebSocketMessage.builder()
                .type(WebSocketMessageType.TITLE_UPDATED)
                .payload(event.payload())
                .build();
        messagingTemplate.convertAndSend(destination, message);
    }

    @EventListener
    public void handleSlideUpdated(final SlideUpdatedEvent event) {
        final String destination = String.format("/topic/presentation.%s.slide.%s", event.presentationId(),
                event.slideId());
        final var response = SlideEditResponse.builder()
                .lastRevisionUserId(event.payload().getLastRevisionUserId())
                .data(event.payload().getData())
                .build();
        messagingTemplate.convertAndSend(destination, response);
    }
}
