package io.wisoft.kimbanana.history.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mapping {
    @JsonProperty("target_slide")
    private String targetSlide;

    @JsonProperty("history_slide")
    private String historySlide;

    public String getTargetSlide() { return targetSlide; }
    public String getHistorySlide() { return historySlide; }
}
