package io.wisoft.kimbanana.workspace.config;

import io.wisoft.kimbanana.workspace.repository.MockPresentationRepository;
import io.wisoft.kimbanana.workspace.service.PresentationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig{
    @Bean
    public MockPresentationRepository presentationRepository() {
        return new MockPresentationRepository();
    }
    @Bean
    public PresentationService presentationService() {
        return new PresentationService(presentationRepository());
    }
}
