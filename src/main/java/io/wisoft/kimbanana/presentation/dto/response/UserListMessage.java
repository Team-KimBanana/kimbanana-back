package io.wisoft.kimbanana.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.kimbanana.presentation.dto.request.ActiveUser;
import java.util.List;

public record UserListMessage(
        @JsonProperty("presentation_id")
        String presentationId,
        
        @JsonProperty("active_users")
        List<ActiveUser> activeUsers,
        
        @JsonProperty("total_count")
        int totalCount
) {}