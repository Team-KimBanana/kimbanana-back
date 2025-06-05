package io.wisoft.kimbanana.presentation.repository;

import io.wisoft.kimbanana.presentation.entity.Slide;

public interface SlideRepository {
    Slide findById(String slideId);

    void update(String slideId, Slide updateParam);

    void delete(String slideId);
}
