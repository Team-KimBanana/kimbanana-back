package io.wisoft.kimbanana.workspace.config;

import io.wisoft.kimbanana.workspace.repository.MockWorkspaceRepository;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig{
    @Bean
    public MockWorkspaceRepository presentationRepository() {
        return new MockWorkspaceRepository();
    }
    @Bean
    public WorkspaceService presentationService() {
        return new WorkspaceService(presentationRepository());
    }
}
