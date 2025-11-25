package io.wisoft.kimbanana.infrastructure.config;

import io.wisoft.kimbanana.history.repository.HistoryRepository;
import io.wisoft.kimbanana.history.repository.jdbc.JdbcHistoryRepository;
import io.wisoft.kimbanana.history.service.HistoryService;
import io.wisoft.kimbanana.image.repository.jdbc.JdbcImageUploadRepository;
import io.wisoft.kimbanana.image.service.ImageService;
import io.wisoft.kimbanana.image.service.StorageService;
import io.wisoft.kimbanana.presentation.repository.jdbc.JdbcPresentationRepository;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
import io.wisoft.kimbanana.workspace.repository.jdbc.JdbcWorkspaceRepository;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import javax.sql.DataSource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final DataSource dataSource;


    public AppConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PresentationRepository presentationRepository() {
        return new JdbcPresentationRepository(dataSource);
    }

    @Bean
    public PresentationService presentationService(ApplicationEventPublisher publisher) {
        return new PresentationService(presentationRepository(), publisher);
    }

    @Bean
    public WorkspaceRepository workspaceRepository() {
        return new JdbcWorkspaceRepository(dataSource);
    }

    @Bean
    public WorkspaceService workspaceService() {
        return new WorkspaceService(workspaceRepository());
    }

    @Bean
    public HistoryRepository historyRepository() {
        return new JdbcHistoryRepository(dataSource);
    }

    @Bean
    public HistoryService historyService() {
        return new HistoryService(historyRepository());
    }

    @Bean
    public JdbcImageUploadRepository imageUploadRepository() {
        return new JdbcImageUploadRepository(dataSource);
    }

    @Bean
    public StorageService storageService() {
        return new StorageService();
    }

    @Bean
    public ImageService imageService() {
        return new ImageService(imageUploadRepository(), storageService());
    }
}

