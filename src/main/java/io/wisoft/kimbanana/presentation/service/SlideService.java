package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.repository.SlideRepository;

public class SlideService {
    private final SlideRepository slideRepository;

    public SlideService(final SlideRepository slideRepository) {
        this.slideRepository = slideRepository;
    }
}
