package io.wisoft.kimbanana.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RestorePayload {
    public String type;

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
