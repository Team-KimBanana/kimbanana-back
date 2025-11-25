package io.wisoft.kimbanana.history.dto;

import java.time.LocalDateTime;

public class HistoryListResponse {
    private final String historyId;
    private final LocalDateTime lastRevisionDate;

    public HistoryListResponse(String historyId, LocalDateTime lastRevisionDate) {
        this.historyId = historyId;
        this.lastRevisionDate = lastRevisionDate;
    }

    public String getHistoryId() {
        return historyId;
    }

    public LocalDateTime getLastRevisionDate() {
        return lastRevisionDate;
    }
}
