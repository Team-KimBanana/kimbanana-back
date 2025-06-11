package io.wisoft.kimbanana.presentation.config;

import io.wisoft.kimbanana.presentation.repository.MockSlideRepository;
import io.wisoft.kimbanana.presentation.repository.mock.MockPresentationRepository;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import io.wisoft.kimbanana.presentation.repository.SlideRepository;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import io.wisoft.kimbanana.presentation.service.SlideService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public SlideRepository slideRepository() {
        return new MockSlideRepository();
    }
    @Bean
    public SlideService slideService() {
        return new SlideService(slideRepository());
    }

    @Bean
    public PresentationRepository presentationRepository() {
        return new MockPresentationRepository();
    }

    @Bean
    public PresentationService presentationService() {
        return new PresentationService(presentationRepository());
    }


}

