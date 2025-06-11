package io.wisoft.kimbanana.config;

import io.wisoft.kimbanana.presentation.repository.mock.MockPresentationRepository;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public PresentationRepository presentationRepository() {
        return new MockPresentationRepository();
    }

    @Bean
    public PresentationService presentationService() {
        return new PresentationService(presentationRepository());
    }

//    @Bean
//    public MockWorkspaceRepository presentationRepository() {
//        return new MockWorkspaceRepository();
//    }
//    @Bean
//    public WorkspaceService presentationService() {
//        return new WorkspaceService(presentationRepository());
//    }

}

