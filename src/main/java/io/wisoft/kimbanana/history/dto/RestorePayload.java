package io.wisoft.kimbanana.history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.kimbanana.history.entity.Mapping;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RestorePayload {
    public String type;

    @JsonProperty("history_id")
    private String historyId;

    @JsonProperty("last_revision_user_id")
    private String currentUserId;

    public List<Mapping> mappings;

    @Builder
    public RestorePayload (String type, String historyId){
        this.type = type;
        this.historyId = historyId;
    }
}
