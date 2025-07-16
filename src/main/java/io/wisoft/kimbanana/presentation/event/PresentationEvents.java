
package io.wisoft.kimbanana.presentation.event;

import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Slide;

public final class PresentationEvents {

    public record SlideAddedEvent(String presentationId, SlideAddPayload payload) {
    }

    public record StructureUpdatedEvent(String presentationId, StructurePayload payload) {
    }

    public record TitleUpdatedEvent(String presentationId, TitlePayload payload) {
    }

    public record SlideUpdatedEvent(String presentationId, String slideId, Slide payload) {
    }

}
