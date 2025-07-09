package io.wisoft.kimbanana.presentation.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Image {
    String id;
    String name;
    String path;
}
