package io.wisoft.kimbanana.config;

import io.wisoft.kimbanana.presentation.repository.jdbc.JdbcPresentationRepository;
import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
import io.wisoft.kimbanana.workspace.repository.jdbc.JdbcWorkspaceRepository;
import io.wisoft.kimbanana.workspace.service.WorkspaceService;
import javax.sql.DataSource;
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
    public PresentationService presentationService() {
        return new PresentationService(presentationRepository());
    }

    @Bean
    public WorkspaceRepository workspaceRepository() {
        return new JdbcWorkspaceRepository(dataSource);
    }
    @Bean
    public WorkspaceService workspaceService() {
        return new WorkspaceService(workspaceRepository());
    }

}

